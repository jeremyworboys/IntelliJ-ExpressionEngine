package com.jeremyworboys.expressionengine;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExpressionEngineFileType extends LanguageFileType {
    public static final ExpressionEngineFileType INSTANCE = new ExpressionEngineFileType();
    @NonNls public static final String DEFAULT_EXTENSION = "html";

    private ExpressionEngineFileType() {
        super(ExpressionEngineLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "ExpressionEngine file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "ExpressionEngine language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return ExpressionEngineIcons.FILE;
    }
}
