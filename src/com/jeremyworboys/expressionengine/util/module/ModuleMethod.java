package com.jeremyworboys.expressionengine.util.module;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.OrderedSet;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModuleMethod {
    private final String tagName;
    private final Method method;

    public ModuleMethod(String tagName, Method method) {
        this.tagName = tagName;
        this.method = method;
    }

    @NotNull
    public List<String> getParameterNames() {
        List<String> parameterNames = new OrderedSet<>();
        ElementPattern<PsiElement> matcher = PlatformPatterns
            .psiElement(PhpElementTypes.METHOD_REFERENCE)
            //.withName("fetch_param") <- This doesn't work, so it is handled below
            .with(new PatternCondition<PsiElement>("withName") {
                @Override
                public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
                    return ((MethodReference) psiElement).getName().equals("fetch_param");
                }
            })
            .withChild(PlatformPatterns
                .psiElement(PhpElementTypes.PARAMETER_LIST)
                .withFirstChild(PlatformPatterns
                    .psiElement(PhpElementTypes.STRING)
                )
            );

        List<PsiElement> foundMethodCalls = searchPsiTree(method, matcher);
        for (PsiElement methodCall : foundMethodCalls) {
            PsiElement[] parameters = ((MethodReference) methodCall).getParameters();
            parameterNames.add(((StringLiteralExpression) parameters[0]).getContents());
        }

        return parameterNames;
    }

    @NotNull
    private List<PsiElement> searchPsiTree(PsiElement root, ElementPattern<PsiElement> matcher) {
        List<PsiElement> results = new ArrayList<>();

        for (PsiElement element : root.getChildren()) {
            if (matcher.accepts(element)) {
                results.add(element);
                continue;
            }
            // TODO: Recurse into called methods
            if (element.getChildren().length > 0) {
                results.addAll(searchPsiTree(element, matcher));
            }
        }

        return results;
    }
}
