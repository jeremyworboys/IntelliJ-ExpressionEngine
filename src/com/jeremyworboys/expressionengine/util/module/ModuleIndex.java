package com.jeremyworboys.expressionengine.util.module;

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

public class ModuleIndex {
    private final Project project;

    public ModuleIndex(@NotNull Project project) {
        this.project = project;
    }

    @NotNull
    public Map<String, ModuleMethod> getMethods() {
        Map<String, ModuleMethod> moduleMethods = new HashMap<>();
        ExpressionEngineAddonUtil expressionEngineAddonUtil = new ExpressionEngineAddonUtil(project);

        for (ExpressionEngineAddon expressionEngineAddon : expressionEngineAddonUtil.getAddons()) {
            moduleMethods.putAll(getAddonModuleMethods(expressionEngineAddon));
        }

        return moduleMethods;
    }

    @Nullable
    public ModuleMethod getModuleMethod(String tagName) {
        return getMethods().get(tagName);
    }

    @NotNull
    private Map<String, ModuleMethod> getAddonModuleMethods(ExpressionEngineAddon expressionEngineAddon) {
        Map<String, ModuleMethod> methods = new HashMap<>();
        String moduleName = StringUtil.toLowerCase(expressionEngineAddon.getName());
        PhpClass moduleClass = expressionEngineAddon.getModuleClass();

        if (moduleClass != null) {
            // Add all public methods
            for (Method method : moduleClass.getMethods()) {
                if (isViableModuleMethod(method)) {
                    String methodName = StringUtil.toLowerCase(method.getName());
                    if (methodName.equals(moduleName)) {
                        String tagName = "exp:" + moduleName;
                        methods.put(tagName, new ModuleMethod(tagName, method));
                    }
                    String tagName = "exp:" + moduleName + ':' + methodName;
                    methods.put(tagName, new ModuleMethod(tagName, method));
                }
            }

            // Add constructor if a method doesn't share the same name as the module
            String tagName = "exp:" + moduleName;
            if (!methods.containsKey(tagName)) {
                Method moduleConstructor = moduleClass.getConstructor();
                if (moduleConstructor != null && isViableModuleMethod(moduleConstructor)) {
                    methods.put(tagName, new ModuleMethod(tagName, moduleConstructor));
                }
            }
        }

        return methods;
    }

    private boolean isViableModuleMethod(@NotNull Method method) {
        return method.getAccess().isPublic() && method.getParameters().length == 0;
    }
}
