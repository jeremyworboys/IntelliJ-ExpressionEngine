package com.jeremyworboys.expressionengine.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineConditional;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExpressionEngineStructureViewElement extends PsiTreeElementBase<PsiElement> implements StructureViewTreeElement {
    private final PsiElement element;

    public ExpressionEngineStructureViewElement(PsiElement element) {
        super(element);
        this.element = element;
    }

    @NotNull
    @Override
    public Collection<StructureViewTreeElement> getChildrenBase() {
        if (element.isValid()) {
            ArrayList<StructureViewTreeElement> children = new ArrayList<>();
            collectChildren(children, element);
            return children;
        }

        return Collections.emptySet();
    }

    @Nullable
    @Override
    public String getPresentableText() {
        if (element instanceof PsiNamedElement) {
            return ((PsiNamedElement) element).getName();
        }

        return (element != null) ? element.getText() : null;
    }

    private static void collectChildren(@NotNull List<StructureViewTreeElement> result, @NotNull PsiElement root) {
        PsiElement[] children = root.getChildren();
        if (children.length == 0) {
            return;
        }

        for (PsiElement child : children) {
            if (child instanceof ExpressionEngineFile) {
                result.add(new ExpressionEngineStructureViewElement(child));
            }
            if (child instanceof ExpressionEngineModule) {
                result.add(new ExpressionEngineStructureViewModule((ExpressionEngineModule) child));
            }
            if (child instanceof ExpressionEngineConditional) {
                result.add(new ExpressionEngineStructureViewConditional((ExpressionEngineConditional) child));
            }
        }
    }
}
