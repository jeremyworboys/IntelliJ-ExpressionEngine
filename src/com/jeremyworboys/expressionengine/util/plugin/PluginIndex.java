package com.jeremyworboys.expressionengine.util.plugin;

import com.intellij.openapi.project.Project;
import com.jeremyworboys.expressionengine.util.TagIndexBase;
import com.jeremyworboys.expressionengine.util.dict.ExpressionEngineAddon;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PluginIndex extends TagIndexBase<PluginMethod> {
    private static final Map<Project, PluginIndex> instances = new HashMap<>();

    private PluginIndex(@NotNull Project project) {
        super(project);
    }

    @NotNull
    public static PluginIndex getInstance(@NotNull Project project) {
        if (!instances.containsKey(project)) {
            instances.put(project, new PluginIndex(project));
        }
        return instances.get(project);
    }


    @Nullable
    @Override
    protected PhpClass getTagClass(@NotNull ExpressionEngineAddon expressionEngineAddon) {
        return expressionEngineAddon.getPluginClass();
    }

    @NotNull
    @Override
    protected PluginMethod createTagMethod(@NotNull String tagName, @NotNull PhpClass tagClass, @NotNull Method method) {
        return new PluginMethod(tagName, tagClass, method);
    }
}
