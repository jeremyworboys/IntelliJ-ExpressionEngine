package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineVariableElement;
import org.jetbrains.annotations.NotNull;

public abstract class ExpressionEngineVariableMixin extends ASTWrapperPsiElement implements ExpressionEngineVariableElement {
    public ExpressionEngineVariableMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isPairVariable() {
        return getVariableCloseTag() != null;
    }
}
