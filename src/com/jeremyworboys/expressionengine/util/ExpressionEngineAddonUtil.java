package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.util.dict.ExpressionEngineAddon;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEngineAddonUtil {
    private static final Pattern addonFilePathPattern = Pattern.compile(".*/(.+?)/(acc|ext|ft|mcp|mod|pi|tab|upd)\\.(\\1)\\.php$");
    private static Map<Project, ExpressionEngineAddonUtil> instances = new HashMap<>();

    private final Project project;
    private final PhpIndex phpIndex;
    private HashMap<String, ExpressionEngineAddon> expressionEngineAddons;

    private ExpressionEngineAddonUtil(@NotNull Project project) {
        this.project = project;
        this.phpIndex = PhpIndex.getInstance(project);
        this.loadAddons();
    }

    @NotNull
    public static ExpressionEngineAddonUtil getInstance(@NotNull Project project) {
        if (!instances.containsKey(project)) {
            instances.put(project, new ExpressionEngineAddonUtil(project));
        }
        return instances.get(project);
    }

    private void loadAddons() {
        expressionEngineAddons = new HashMap<>();
        // TODO: Look into how EE loads add-ons to optimise this
        Collection<VirtualFile> phpFiles = FileBasedIndex.getInstance()
            .getContainingFiles(FileTypeIndex.NAME, PhpFileType.INSTANCE, GlobalSearchScope.allScope(project));

        for (VirtualFile phpFile : phpFiles) {
            Matcher matcher = addonFilePathPattern.matcher(phpFile.getPath());
            if (matcher.matches()) {
                String addonName = matcher.group(1);
                String addonType = matcher.group(2);
                if (!expressionEngineAddons.containsKey(addonName)) {
                    PhpClass addonClass = phpIndex.getClassByName(
                        ExpressionEngineAddonClassUtil.getAddonClassName(addonName, addonType));
                    if (addonClass != null) {
                        expressionEngineAddons.putIfAbsent(addonName,
                            new ExpressionEngineAddon(addonClass.getContainingFile().getParent()));
                    }
                }
            }
        }
    }

    public Collection<ExpressionEngineAddon> getAddons() {
        return this.expressionEngineAddons.values();
    }
}
