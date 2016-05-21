package com.jeremyworboys.expressionengine.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.jeremyworboys.expressionengine.ExpressionEngineFileType;
import com.jeremyworboys.expressionengine.ExpressionEngineLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ExpressionEngineFile extends PsiFileBase {
    public ExpressionEngineFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ExpressionEngineLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ExpressionEngineFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "ExpressionEngine File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
