package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamElement;
import org.jetbrains.annotations.NotNull;

public abstract class ExpressionEngineTagParamMixin extends ASTWrapperPsiElement implements ExpressionEngineTagParamElement {
    public ExpressionEngineTagParamMixin(@NotNull ASTNode node) {
        super(node);
    }
}
