package com.jeremyworboys.expressionengine.util;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.OrderedSet;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhpElementsUtil {
    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodReference() {
        return PlatformPatterns.psiElement(PhpElementTypes.METHOD_REFERENCE);
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodReferenceNamed(String name) {
        return PhpElementsUtil
            .isMethodReference()
            //.withName(name) <- This doesn't work, so it is handled below
            .with(new PatternCondition<PsiElement>("withName") {
                @Override
                public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
                    String elementName = ((MethodReference) psiElement).getName();
                    assert elementName != null;
                    return elementName.equals(name);
                }
            });
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodReferenceWithFirstStringNamed(String name) {
        return PhpElementsUtil
            .isMethodReferenceNamed(name)
            .withChild(PlatformPatterns
                .psiElement(PhpElementTypes.PARAMETER_LIST)
                .withFirstChild(PlatformPatterns
                    .psiElement(PhpElementTypes.STRING)
                )
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
}
