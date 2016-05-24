package com.jeremyworboys.expressionengine.util.plugin;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.OrderedSet;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PluginMethod {
    private final String tagName;
    private final PhpClass phpClass;
    private final Method phpMethod;

    public PluginMethod(String tagName, PhpClass phpClass, Method phpMethod) {
        this.tagName = tagName;
        this.phpClass = phpClass;
        this.phpMethod = phpMethod;
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
