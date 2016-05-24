package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.util.dict.ExpressionEngineAddon;
import com.jetbrains.php.lang.PhpFileType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEngineAddonUtil {
    private final Project project;
    private final Pattern addonFilePathPattern = Pattern.compile(".*/(.+?)/(acc|ext|ft|mcp|mod|pi|tab|upd)\\.(\\1)\\.php$");
    private HashMap<String, ExpressionEngineAddon> expressionEngineAddons;

    public ExpressionEngineAddonUtil(@NotNull Project project) {
        this.project = project;
        this.loadAddons();
    }

    private void loadAddons() {
        expressionEngineAddons = new HashMap<>();
        Collection<VirtualFile> phpFiles = FileBasedIndex.getInstance()
            .getContainingFiles(FileTypeIndex.NAME, PhpFileType.INSTANCE, GlobalSearchScope.allScope(project));

        for (VirtualFile phpFile : phpFiles) {
            Matcher matcher = addonFilePathPattern.matcher(phpFile.getPath());
            if (matcher.matches()) {
                String addonName = matcher.group(1);
                expressionEngineAddons.putIfAbsent(addonName, new ExpressionEngineAddon(phpFile.getParent()));
            }
        }
    }

    public Collection<ExpressionEngineAddon> getAddons() {
        return this.expressionEngineAddons.values();
    }
}
