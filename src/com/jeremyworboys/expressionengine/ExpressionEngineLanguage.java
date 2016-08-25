package com.jeremyworboys.expressionengine;

import com.intellij.lang.Language;
import com.intellij.psi.templateLanguages.TemplateLanguage;

public class ExpressionEngineLanguage extends Language implements TemplateLanguage {
    public static final ExpressionEngineLanguage INSTANCE = new ExpressionEngineLanguage();

    private ExpressionEngineLanguage() {
        super("ExpressionEngine");
    }
}
