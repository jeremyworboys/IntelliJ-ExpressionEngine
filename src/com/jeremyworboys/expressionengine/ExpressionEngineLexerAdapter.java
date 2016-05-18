package com.jeremyworboys.expressionengine;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class ExpressionEngineLexerAdapter extends FlexAdapter {
    public ExpressionEngineLexerAdapter() {
        super(new ExpressionEngineLexer((Reader) null));
    }
}
