package com.jeremyworboys.expressionengine.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jeremyworboys.expressionengine.folding.descriptor.VariableFoldingDescriptor;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VariableFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        Collection<ExpressionEngineVariable> variableElements = PsiTreeUtil.findChildrenOfType(root, ExpressionEngineVariable.class);
        for (ExpressionEngineVariable variableElement : variableElements) {
            if (variableElement.isPairVariable()) {
                descriptors.add(new VariableFoldingDescriptor(variableElement));
            }
        }

        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
