package com.jeremyworboys.expressionengine.util.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.jeremyworboys.expressionengine.util.ExpressionEngineAddonUtil;
import com.jeremyworboys.expressionengine.util.dict.ExpressionEngineAddon;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PluginIndex {
    private static Map<Project, PluginIndex> instances = new HashMap<>();
    private final Project project;

    private PluginIndex(@NotNull Project project) {
        this.project = project;
    }

    public static PluginIndex getInstance(@NotNull Project project) {
        if (!instances.containsKey(project)) {
            instances.put(project, new PluginIndex(project));
        }
        return instances.get(project);
    }

    @NotNull
    public Map<String, PluginMethod> getMethods() {
        Map<String, PluginMethod> pluginMethods = new HashMap<>();
        ExpressionEngineAddonUtil expressionEngineAddonUtil = new ExpressionEngineAddonUtil(project);

        for (ExpressionEngineAddon expressionEngineAddon : expressionEngineAddonUtil.getAddons()) {
            pluginMethods.putAll(getAddonPluginMethods(expressionEngineAddon));
        }

        return pluginMethods;
    }

    @Nullable
    public PluginMethod getPluginMethod(String tagName) {
        return getMethods().get(tagName);
    }

    @NotNull
    private Map<String, PluginMethod> getAddonPluginMethods(ExpressionEngineAddon expressionEngineAddon) {
        Map<String, PluginMethod> methods = new HashMap<>();
        String pluginName = StringUtil.toLowerCase(expressionEngineAddon.getName());
        PhpClass pluginClass = expressionEngineAddon.getPluginClass();

        if (pluginClass != null) {
            // Add all public methods
            for (Method method : pluginClass.getMethods()) {
                if (isViablePluginMethod(method)) {
                    String methodName = StringUtil.toLowerCase(method.getName());
                    if (methodName.equals(pluginName)) {
                        String tagName = "exp:" + pluginName;
                        methods.put(tagName, new PluginMethod(tagName, pluginClass, method));
                    }
                    String tagName = "exp:" + pluginName + ':' + methodName;
                    methods.put(tagName, new PluginMethod(tagName, pluginClass, method));
                }
            }

            // Add constructor if a method doesn't share the same name as the plugin
            String tagName = "exp:" + pluginName;
            if (!methods.containsKey(tagName)) {
                Method pluginConstructor = pluginClass.getConstructor();
                if (pluginConstructor != null && isViablePluginConstructor(pluginConstructor)) {
                    methods.put(tagName, new PluginMethod(tagName, pluginClass, pluginConstructor));
                }
            }
        }

        return methods;
    }

    private boolean isViablePluginMethod(@NotNull Method method) {
        return method.getAccess().isPublic()
            && !method.getName().startsWith("_")
            && method.getParameters().length == 0;
    }

    private boolean isViablePluginConstructor(@NotNull Method method) {
        return method.getAccess().isPublic()
            && method.getParameters().length == 0;
    }
}
