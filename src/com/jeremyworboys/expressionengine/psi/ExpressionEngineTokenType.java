package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.template.ExpressionEngineLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineTokenType extends IElementType {
    public ExpressionEngineTokenType(@NotNull @NonNls String debugName) {
        super(debugName, ExpressionEngineLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "ExpressionEngineTokenType." + super.toString();
    }
}
