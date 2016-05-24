package com.jeremyworboys.expressionengine.util.module;

import com.intellij.psi.PsiElement;
import com.intellij.util.containers.OrderedSet;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModuleMethod {
    private final String tagName;
    private final PhpClass phpClass;
    private final Method phpMethod;

    public ModuleMethod(String tagName, PhpClass phpClass, Method phpMethod) {
        this.tagName = tagName;
        this.phpClass = phpClass;
        this.phpMethod = phpMethod;
    }

    @NotNull
    public List<String> getParameterNames() {
        List<String> parameterNames = new OrderedSet<>();
        List<MethodReference> fetchParamMethodCalls = PhpElementsUtil
            .getLogicalDescendantOf(method, PhpElementsUtil.isMethodReferenceWithFirstStringNamed("fetch_param"));

        for (MethodReference fetchParamMethodCall : fetchParamMethodCalls) {
            PsiElement[] parameters = fetchParamMethodCall.getParameters();
            parameterNames.add(((StringLiteralExpression) parameters[0]).getContents());
        }

        return parameterNames;
    }
}
