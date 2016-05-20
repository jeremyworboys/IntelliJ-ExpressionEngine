package com.jeremyworboys.expressionengine.parser.psi;

import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.ExpressionEngineLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineElementType extends IElementType {
    public ExpressionEngineElementType(@NotNull @NonNls String debugName) {
        super(debugName, ExpressionEngineLanguage.INSTANCE);
    }
}
