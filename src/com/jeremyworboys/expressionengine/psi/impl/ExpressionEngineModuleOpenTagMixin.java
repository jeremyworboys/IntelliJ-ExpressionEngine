package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTagElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import com.jeremyworboys.expressionengine.referencing.ModuleNameReference;
import org.jetbrains.annotations.NotNull;

public abstract class ExpressionEngineModuleOpenTagMixin extends ASTWrapperPsiElement implements ExpressionEngineModuleOpenTagElement {
    public ExpressionEngineModuleOpenTagMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getModuleName() {
        return this.getNode().findChildByType(ExpressionEngineTypes.T_MODULE_NAME).getText();
    }

    @Override
    public PsiReference getReference() {
        TextRange textRange = new TextRange(1, getModuleName().length() + 1);
        return new ModuleNameReference((ExpressionEngineModuleOpenTag) this, textRange);
    }
}
