package com.jeremyworboys.expressionengine;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineFileViewProviderFactory implements FileViewProviderFactory{
    public ExpressionEngineFileViewProviderFactory() {
    }

    @NotNull
    @Override
    public FileViewProvider createFileViewProvider(@NotNull VirtualFile file, Language language, @NotNull PsiManager manager, boolean eventSystemEnabled) {
        return new ExpressionEngineFileViewProvider(manager, file, eventSystemEnabled);
    }
}
