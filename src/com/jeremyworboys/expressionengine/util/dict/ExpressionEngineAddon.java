package com.jeremyworboys.expressionengine.util.dict;

import com.intellij.openapi.vfs.VirtualFile;

public class ExpressionEngineAddon {
    private final String addonName;
    private final VirtualFile addonDirectory;

    public ExpressionEngineAddon(VirtualFile addonDirectory) {
        if (!addonDirectory.isDirectory()) {
            throw new IllegalArgumentException("Add-on directory must be a directory.");
        }
        this.addonName = addonDirectory.getName();
        this.addonDirectory = addonDirectory;
    }

    public String getAddonName() {
        return addonName;
    }

    public VirtualFile getAddonDirectory() {
        return addonDirectory;
    }
}
