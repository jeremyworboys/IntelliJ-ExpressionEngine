package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTagElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;

public abstract class ExpressionEngineModuleOpenTagMixin extends ASTWrapperPsiElement implements ExpressionEngineModuleOpenTagElement {
    public ExpressionEngineModuleOpenTagMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getModuleName() {
        return this.getNode().findChildByType(ExpressionEngineTypes.T_MODULE_NAME).getText();
    }
}
