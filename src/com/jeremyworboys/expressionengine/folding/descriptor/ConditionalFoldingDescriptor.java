package com.jeremyworboys.expressionengine.folding.descriptor;

import com.intellij.lang.folding.FoldingDescriptor;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineConditional;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConditionalFoldingDescriptor extends FoldingDescriptor {
    @NotNull
    private final ExpressionEngineConditional element;

    public ConditionalFoldingDescriptor(@NotNull ExpressionEngineConditional element) {
        super(element, element.getTextRange());
        this.element = element;
    }

    @Nullable
    @Override
    public String getPlaceholderText() {
        ExpressionEngineExpr expression = element.getConditionalIf().getExpr();
        if (expression == null) {
            return null;
        }

        String expressionText = expression.getText();
        if (expressionText == null) {
            return null;
        }

        return "{if " + expressionText.replaceAll("\\n\\s+", " ") + " ...}";
    }
}
