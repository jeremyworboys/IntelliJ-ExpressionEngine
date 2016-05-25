package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineVariableOpenTagElement;
import org.jetbrains.annotations.NotNull;

public abstract class ExpressionEngineVariableOpenTagMixin extends ASTWrapperPsiElement implements ExpressionEngineVariableOpenTagElement {
    public ExpressionEngineVariableOpenTagMixin(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public String getVariableName() {
        return this.getNode().findChildByType(ExpressionEngineTypes.T_VARIABLE_NAME).getText();
    }
}
