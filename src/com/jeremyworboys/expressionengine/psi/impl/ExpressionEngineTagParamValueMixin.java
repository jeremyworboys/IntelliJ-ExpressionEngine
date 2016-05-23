package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.jeremyworboys.expressionengine.psi.*;
import com.jeremyworboys.expressionengine.referencing.TemplateReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpressionEngineTagParamValueMixin extends ASTWrapperPsiElement implements ExpressionEngineTagParamValueElement {
    public ExpressionEngineTagParamValueMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        ASTNode literalNode = this.getNode().getFirstChildNode();

        if (literalNode.getElementType() == ExpressionEngineTypes.PATH_LITERAL) {
            ExpressionEnginePathLiteral pathLiteral = ExpressionEngineElementFactory.createPathLiteral(getProject(), name);
            this.getFirstChild().replace(pathLiteral);
        }

        if (literalNode.getElementType() == ExpressionEngineTypes.STRING_LITERAL) {
            ExpressionEngineStringLiteral stringLiteral = ExpressionEngineElementFactory.createStringLiteral(getProject(), name);
            this.getFirstChild().replace(stringLiteral);
        }

        return this;
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
        List<PsiReference> references = new ArrayList<PsiReference>();
        IElementType type = this.getNode().getFirstChildNode().getElementType();

        if (type == ExpressionEngineTypes.PATH_LITERAL) {
            String paramValue = getText();
            TextRange textRange = new TextRange(0, paramValue.length());
            references.add(new TemplateReference((ExpressionEngineTagParamValue) this, textRange));
        }

        if (type == ExpressionEngineTypes.STRING_LITERAL) {
            String paramValue = StringUtil.stripQuotesAroundValue(getText());
            TextRange textRange = new TextRange(1, paramValue.length() + 1);
            references.add(new TemplateReference((ExpressionEngineTagParamValue) this, textRange));
        }

        return references.toArray(new PsiReference[references.size()]);
    }
}
