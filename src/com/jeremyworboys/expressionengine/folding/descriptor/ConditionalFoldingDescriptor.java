package com.jeremyworboys.expressionengine.folding.descriptor;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineConditional;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConditionalFoldingDescriptor extends FoldingDescriptor {
    @NotNull
    private final ExpressionEngineConditional element;

    public ConditionalFoldingDescriptor(@NotNull ExpressionEngineConditional element, @Nullable FoldingGroup group) {
        super(element.getNode(), getTextRange(element), group);
        this.element = element;
    }

    @NotNull
    private static TextRange getTextRange(@NotNull ExpressionEngineConditional element) {
        return new TextRange(element.getTextRange().getStartOffset() + 4, element.getTextRange().getEndOffset() - 1);
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

        return expressionText.replaceAll("\\s+", " ");
    }
}
