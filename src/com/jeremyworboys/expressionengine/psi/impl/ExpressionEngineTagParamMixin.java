package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ExpressionEngineTagParamMixin extends ASTWrapperPsiElement implements ExpressionEngineTagParamElement {
    public ExpressionEngineTagParamMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getTagParamName() {
        return this.getNode().findChildByType(ExpressionEngineTypes.T_PARAM_NAME).getText();
    }

    @Nullable
    @Override
    public ExpressionEngineModuleOpenTag getModuleOpenTag() {
        for (PsiElement cur = getParent(); cur.getParent() != null; cur = cur.getParent()) {
            if (cur.getNode().getElementType() == ExpressionEngineTypes.MODULE_OPEN_TAG) {
                return (ExpressionEngineModuleOpenTag) cur;
            }
        }

        return null;
    }
}
