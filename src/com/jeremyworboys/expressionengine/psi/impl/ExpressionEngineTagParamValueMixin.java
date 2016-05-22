package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamValue;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import com.jeremyworboys.expressionengine.referencing.TemplateReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ExpressionEngineTagParamValueMixin extends ASTWrapperPsiElement {
    public ExpressionEngineTagParamValueMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public PsiReference getReference() {
        PsiReference[] references = getReferences();
        if (references.length > 0) {
            return references[0];
        }
        return null;
    }

    @Override
    @NotNull
    public PsiReference[] getReferences() {
        IElementType type = this.getNode().getFirstChildNode().getElementType();
        if (type != ExpressionEngineTypes.T_PATH && type != ExpressionEngineTypes.STRING_LITERAL) {
            return PsiReference.EMPTY_ARRAY;
        }
        return new PsiReference[]{new TemplateReference((ExpressionEngineTagParamValue) this)};
    }
}
