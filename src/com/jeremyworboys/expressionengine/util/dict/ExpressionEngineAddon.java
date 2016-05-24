package com.jeremyworboys.expressionengine.util.dict;

import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineAddon {
    private final String name;
    private final PsiDirectory directory;

    public ExpressionEngineAddon(PsiDirectory directory) {
        this.name = directory.getName();
        this.directory = directory;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public PsiDirectory getDirectory() {
        return directory;
    }
}
