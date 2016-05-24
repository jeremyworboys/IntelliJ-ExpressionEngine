package com.jeremyworboys.expressionengine.util.dict;

import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineAddon {
    private final String addonName;
    private final PsiDirectory addonDirectory;

    public ExpressionEngineAddon(PsiDirectory addonDirectory) {
        this.addonName = addonDirectory.getName();
        this.addonDirectory = addonDirectory;
    }

    @NotNull
    public String getAddonName() {
        return addonName;
    }

    @NotNull
    public PsiDirectory getAddonDirectory() {
        return addonDirectory;
    }
}
