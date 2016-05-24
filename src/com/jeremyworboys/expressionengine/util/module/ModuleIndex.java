package com.jeremyworboys.expressionengine.util.module;

import com.intellij.openapi.project.Project;
import com.jeremyworboys.expressionengine.util.TagIndexBase;
import com.jeremyworboys.expressionengine.util.dict.ExpressionEngineAddon;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModuleIndex extends TagIndexBase<ModuleMethod> {
    private static Map<Project, ModuleIndex> instances = new HashMap<>();

    private ModuleIndex(@NotNull Project project) {
        super(project);
    }

    @NotNull
    public static ModuleIndex getInstance(@NotNull Project project) {
        if (!instances.containsKey(project)) {
            instances.put(project, new ModuleIndex(project));
        }
        return instances.get(project);
    }

    @Nullable
    @Override
    protected PhpClass getTagClass(@NotNull ExpressionEngineAddon expressionEngineAddon) {
        return expressionEngineAddon.getModuleClass();
    }

    @NotNull
    @Override
    protected ModuleMethod createTagMethod(@NotNull String tagName, @NotNull PhpClass tagClass, @NotNull Method method) {
        return new ModuleMethod(tagName, tagClass, method);
    }
}
