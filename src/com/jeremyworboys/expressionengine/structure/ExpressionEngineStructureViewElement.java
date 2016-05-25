package com.jeremyworboys.expressionengine.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private final PsiElement element;

    public ExpressionEngineStructureViewElement(PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return null;
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return null;
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        return new TreeElement[0];
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof NavigationItem) {
            ((NavigationItem) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        if (element instanceof NavigationItem) {
            ((NavigationItem) element).canNavigate();
        }

        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        if (element instanceof NavigationItem) {
            ((NavigationItem) element).canNavigateToSource();
        }

        return false;
    }
}
