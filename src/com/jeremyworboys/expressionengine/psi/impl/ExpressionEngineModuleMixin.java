package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleElement;
import org.jetbrains.annotations.NotNull;

public abstract class ExpressionEngineModuleMixin extends ASTWrapperPsiElement implements ExpressionEngineModuleElement {
    public ExpressionEngineModuleMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isPairModule() {
        return getModuleCloseTag() != null;
    }
}
