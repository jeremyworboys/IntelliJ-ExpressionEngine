package com.jeremyworboys.expressionengine;

import com.intellij.lang.Language;

public class ExpressionEngineLanguage extends Language {
    public static final ExpressionEngineLanguage INSTANCE = new ExpressionEngineLanguage();

    private ExpressionEngineLanguage() {
        super("ExpressionEngine");
    }
}
