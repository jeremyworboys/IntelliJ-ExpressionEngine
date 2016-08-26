package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.OrderedSet;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PhpElementsUtil {
    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodReference() {
        return PlatformPatterns.psiElement(PhpElementTypes.METHOD_REFERENCE);
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodReferenceNamed(String... methodName) {
        return PhpElementsUtil
            .isMethodReference()
            //.withName(name) <- This doesn't work, so it is handled below
            .with(new PatternCondition<PsiElement>("withName") {
                @Override
                public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
                    String methodRefName = ((MethodReference) psiElement).getName();
                    return methodRefName != null && Arrays.asList(methodName).contains(methodRefName);
                }
            });
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodReferenceWithFirstStringNamed(String... methodName) {
        return PhpElementsUtil
            .isMethodReferenceNamed(methodName)
            .withChild(PlatformPatterns
                .psiElement(PhpElementTypes.PARAMETER_LIST)
                .withFirstChild(PlatformPatterns
                    .psiElement(PhpElementTypes.STRING)
                )
            );
    }

    public static PsiElementPattern.Capture<PsiElement> isMethodWithFirstStringOrFieldReference(String... methodName) {
        return PhpElementsUtil
            .isMethodReferenceNamed(methodName)
            .withChild(PlatformPatterns
                .psiElement(PhpElementTypes.PARAMETER_LIST)
                .withFirstChild(PlatformPatterns.or(
                    PlatformPatterns.psiElement(PhpElementTypes.STRING),
                    PlatformPatterns.psiElement(PhpElementTypes.FIELD_REFERENCE),
                    PlatformPatterns.psiElement(PhpElementTypes.CLASS_CONSTANT_REFERENCE)
                ))
            );
    }

    @NotNull
    public static <T extends PsiElement> List<T> getLogicalDescendantOf(@Nullable PsiElement root, @NotNull ElementPattern<PsiElement> pattern) {
        return getLogicalDescendantOf(root, pattern, new OrderedSet<>());
    }

    @NotNull
    // This is used primarily to find usages of TMPL->fetch_param() for annotating module params
    // TODO: I haven't found any other plugins that do this so consider if it is actually a good idea
    private static <T extends PsiElement> List<T> getLogicalDescendantOf(@Nullable PsiElement root, @NotNull ElementPattern<PsiElement> pattern, @NotNull Collection<PsiElement> visited) {
        List<T> results = new ArrayList<>();

        if (root == null) {
            return results;
        }

        for (PsiElement element : root.getChildren()) {
            // Ignore elements that have been scanned previously
            if (visited.contains(element)) {
                continue;
            }
            visited.add(element);
            // Found an element matching pattern - don't recurse
            if (pattern.accepts(element)) {
                //noinspection unchecked
                results.add((T) element);
                continue;
            }
            // Recurse into method calls from this root
            if (isMethodReference().accepts(element)) {
                PsiElement method = ((MethodReference) element).resolve();
                if (method != null) {
                    results.addAll(getLogicalDescendantOf(method, pattern, visited));
                    continue;
                }
            }
            // Recurse over children of this element
            if (element.getChildren().length > 0) {
                results.addAll(getLogicalDescendantOf(element, pattern, visited));
            }
        }

        return results;
    }

    @Nullable
    public static ArrayCreationExpression getReturnedArrayFromFile(PhpFile phpFile) {
        Collection<PhpReturn> phpReturns = PsiTreeUtil.findChildrenOfType(phpFile, PhpReturn.class);
        for (PhpReturn phpReturn : phpReturns) {
            // TODO: What about if this is returning an array in a variable
            if (phpReturn.getArgument() instanceof ArrayCreationExpression) {
                return (ArrayCreationExpression) phpReturn.getArgument();
            }
        }
        return null;
    }

    @Nullable
    public static PhpPsiElement getValueOfKeyInArray(ArrayCreationExpression phpArray, String key) {
        for (ArrayHashElement phpArrayElement : phpArray.getHashElements()) {
            if (phpArrayElement.getKey() instanceof StringLiteralExpression) {
                String phpArrayElementKey = getStringValue(phpArrayElement);
                if (phpArrayElementKey != null && StringUtil.equals(key, phpArrayElementKey)) {
                    return phpArrayElement.getValue();
                }
            }
        }

        return null;
    }

    @Nullable
    public static String getStringValue(@Nullable PsiElement psiElement) {
        return getStringValue(psiElement, 0);
    }

    @Nullable
    private static String getStringValue(@Nullable PsiElement psiElement, int depth) {
        if (psiElement == null || ++depth > 5) {
            return null;
        }

        if (psiElement instanceof StringLiteralExpression) {
            String resolvedString = ((StringLiteralExpression) psiElement).getContents();
            if (StringUtil.isEmpty(resolvedString)) {
                return null;
            }

            return resolvedString;
        }

        if (psiElement instanceof Field) {
            return getStringValue(((Field) psiElement).getDefaultValue(), depth);
        }

        if (psiElement instanceof PhpReference) {
            PsiReference psiReference = psiElement.getReference();
            if (psiReference == null) {
                return null;
            }

            PsiElement ref = psiReference.resolve();
            if (ref instanceof PhpReference) {
                return getStringValue(psiElement, depth);
            }

            if (ref instanceof Field) {
                PsiElement resolved = ((Field) ref).getDefaultValue();

                if (resolved instanceof StringLiteralExpression) {
                    return ((StringLiteralExpression) resolved).getContents();
                }
            }

        }

        return null;
    }
}
