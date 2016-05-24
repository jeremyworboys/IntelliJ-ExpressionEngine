package com.jeremyworboys.expressionengine.util;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PhpElementsUtil {
    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethod() {
        return PlatformPatterns.psiElement(PhpElementTypes.METHOD_REFERENCE);
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodNamed(String name) {
        return PhpElementsUtil
            .isMethod()
            //.withName(name) <- This doesn't work, so it is handled below
            .with(new PatternCondition<PsiElement>("withName") {
                @Override
                public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
                    return ((MethodReference) psiElement).getName().equals(name);
                }
            });
    }

    @NotNull
    public static PsiElementPattern.Capture<PsiElement> isMethodNameWithFirstString(String name) {
        return PhpElementsUtil
            .isMethodNamed(name)
            .withChild(PlatformPatterns
                .psiElement(PhpElementTypes.PARAMETER_LIST)
                .withFirstChild(PlatformPatterns
                    .psiElement(PhpElementTypes.STRING)
                )
            );
    }

    @NotNull
    public static <T extends PsiElement> List<T> getLogicalDescendantOf(@Nullable PsiElement root, @NotNull ElementPattern<PsiElement> pattern) {
        List<T> results = new ArrayList<>();

        if (root == null) {
            return results;
        }

        for (PsiElement element : root.getChildren()) {
            if (pattern.accepts(element)) {
                results.add((T) element);
                continue;
            }
            // TODO: Recurse into called methods
            if (element.getChildren().length > 0) {
                results.addAll(getLogicalDescendantOf(element, pattern));
            }
        }

        return results;
    }
}
