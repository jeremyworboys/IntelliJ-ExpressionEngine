package com.jeremyworboys.expressionengine.util.dict;

import com.intellij.psi.PsiDirectory;
import com.jeremyworboys.expressionengine.util.ExpressionEngineAddonClassUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    public PhpClass getModuleClass() {
        return PhpIndex.getInstance(directory.getProject())
            .getClassByName(ExpressionEngineAddonClassUtil.getModuleClassName(name));
    }

    @Nullable
    public PhpClass getPluginClass() {
        return PhpIndex.getInstance(directory.getProject())
            .getClassByName(ExpressionEngineAddonClassUtil.getPluginClassName(name));
    }
}
