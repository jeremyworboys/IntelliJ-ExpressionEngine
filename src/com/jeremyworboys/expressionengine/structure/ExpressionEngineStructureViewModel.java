package com.jeremyworboys.expressionengine.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    public ExpressionEngineStructureViewModel(@NotNull PsiFile psiFile) {
        super(psiFile, new ExpressionEngineStructureViewElement(psiFile));
    }

    @NotNull
    @Override
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof ExpressionEngineFile;
    }
}
