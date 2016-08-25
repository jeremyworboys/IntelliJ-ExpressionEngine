package com.jeremyworboys.expressionengine.template;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.psi.templateLanguages.TemplateLanguage;

public class ExpressionEngineLanguage extends Language implements TemplateLanguage {
    public static final ExpressionEngineLanguage INSTANCE = new ExpressionEngineLanguage();

    private ExpressionEngineLanguage() {
        super("ExpressionEngine");
    }

    @Override
    public LanguageFileType getAssociatedFileType() {
        return ExpressionEngineFileType.INSTANCE;
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }
}
