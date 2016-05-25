package com.jeremyworboys.expressionengine.parser;

import com.intellij.lexer.FlexAdapter;

public class ExpressionEngineLexerAdapter extends FlexAdapter {
    public ExpressionEngineLexerAdapter() {
        super(new ExpressionEngineLexer(null));
    }
}
