package com.jeremyworboys.expressionengine.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModule;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleCloseTag;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpressionEngineFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        FoldingGroup group = FoldingGroup.newGroup("expressionengine");
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        Collection<ExpressionEngineModule> moduleElements = PsiTreeUtil.findChildrenOfType(root, ExpressionEngineModule.class);
        for (ExpressionEngineModule moduleElement : moduleElements) {
            ExpressionEngineModuleOpenTag openTag = moduleElement.getModuleOpenTag();
            ExpressionEngineModuleCloseTag closeTag = moduleElement.getModuleCloseTag();
            if (closeTag != null) {
                TextRange textRange = new TextRange(moduleElement.getTextRange().getStartOffset() + 1, moduleElement.getTextRange().getEndOffset() - 1);
                descriptors.add(new FoldingDescriptor(moduleElement.getNode(), textRange, group) {
                    @Nullable
                    @Override
                    public String getPlaceholderText() {
                        return openTag.getModuleName();
                    }
                });
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
