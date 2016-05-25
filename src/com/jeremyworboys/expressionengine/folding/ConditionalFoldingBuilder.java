package com.jeremyworboys.expressionengine.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jeremyworboys.expressionengine.folding.descriptor.ConditionalFoldingDescriptor;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineConditional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConditionalFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        Collection<ExpressionEngineConditional> conditionals = PsiTreeUtil.findChildrenOfType(root, ExpressionEngineConditional.class);
        for (ExpressionEngineConditional conditional : conditionals) {
            if (conditional.getConditionalEndif() != null) {
                descriptors.add(new ConditionalFoldingDescriptor(conditional));
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
