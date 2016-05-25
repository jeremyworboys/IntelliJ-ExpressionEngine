package com.jeremyworboys.expressionengine.structure;

import com.jeremyworboys.expressionengine.psi.ExpressionEngineConditional;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineExpr;
import com.jetbrains.php.PhpIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExpressionEngineStructureViewConditional extends ExpressionEngineStructureViewElement {
    private final ExpressionEngineConditional element;

    public ExpressionEngineStructureViewConditional(ExpressionEngineConditional element) {
        super(element);
        this.element = element;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        ExpressionEngineExpr expr = element.getConditionalIf().getExpr();
        if (expr == null) {
            return null;
        }

        return "{if " + expr.getText().replaceAll("\\s+", " ") + "}";
    }

    @Override
    public Icon getIcon(boolean open) {
        return PhpIcons.INTERFACE_ICON;
    }
}
