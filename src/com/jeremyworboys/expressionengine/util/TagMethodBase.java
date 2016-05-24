package com.jeremyworboys.expressionengine.util;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class TagMethodBase {
    private final String tagName;
    private final PhpClass phpClass;
    private final Method phpMethod;

    public TagMethodBase(@NotNull String tagName, @NotNull PhpClass phpClass, @NotNull Method phpMethod) {
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

    @Nullable
    public MethodReference getParameterName(String paramName) {
        return getParameterNamesImpl().get(paramName);
    }

    @NotNull
    public Collection<String> getParameterNames() {
        return getParameterNamesImpl().keySet();
    }

    @NotNull
    private Map<String, MethodReference> getParameterNamesImpl() {
        Map<String, MethodReference> parameterNames = new HashMap<>();
        PsiElementPattern.Capture<PsiElement> fetchParamMethodPattern = PhpElementsUtil
            .isMethodReferenceWithFirstStringNamed("fetch_param");

        List<MethodReference> fetchParamMethodCalls = new ArrayList<>();
        fetchParamMethodCalls.addAll(PhpElementsUtil.getLogicalDescendantOf(phpClass.getOwnConstructor(), fetchParamMethodPattern));
        fetchParamMethodCalls.addAll(PhpElementsUtil.getLogicalDescendantOf(phpMethod, fetchParamMethodPattern));

        for (MethodReference fetchParamMethodCall : fetchParamMethodCalls) {
            PsiElement[] parameters = fetchParamMethodCall.getParameters();
            String paramName = ((StringLiteralExpression) parameters[0]).getContents();
            parameterNames.putIfAbsent(paramName, fetchParamMethodCall);
        }

        return parameterNames;
    }
}
