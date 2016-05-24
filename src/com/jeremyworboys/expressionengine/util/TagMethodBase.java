package com.jeremyworboys.expressionengine.util;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.OrderedSet;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class TagMethodBase {
    private final String tagName;
    private final PhpClass phpClass;
    private final Method phpMethod;

    public TagMethodBase(@NotNull  String tagName, @NotNull  PhpClass phpClass, @NotNull  Method phpMethod) {
        this.tagName = tagName;
        this.phpClass = phpClass;
        this.phpMethod = phpMethod;
    }

    @NotNull
    public String getTagName() {
        return tagName;
    }

    @NotNull
    public PhpClass getPhpClass() {
        return phpClass;
    }

    @NotNull
    public Method getPhpMethod() {
        return phpMethod;
    }

    @NotNull
    public List<String> getParameterNames() {
        List<String> parameterNames = new OrderedSet<>();
        PsiElementPattern.Capture<PsiElement> fetchParamMethodPattern = PhpElementsUtil
            .isMethodReferenceWithFirstStringNamed("fetch_param");

        List<MethodReference> fetchParamMethodCalls = new ArrayList<>();
        fetchParamMethodCalls.addAll(PhpElementsUtil.getLogicalDescendantOf(phpClass.getOwnConstructor(), fetchParamMethodPattern));
        fetchParamMethodCalls.addAll(PhpElementsUtil.getLogicalDescendantOf(phpMethod, fetchParamMethodPattern));

        for (MethodReference fetchParamMethodCall : fetchParamMethodCalls) {
            PsiElement[] parameters = fetchParamMethodCall.getParameters();
            parameterNames.add(((StringLiteralExpression) parameters[0]).getContents());
        }

        return parameterNames;
    }
}
