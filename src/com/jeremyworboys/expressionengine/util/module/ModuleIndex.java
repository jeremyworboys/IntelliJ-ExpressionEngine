package com.jeremyworboys.expressionengine.util.module;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.jeremyworboys.expressionengine.util.ExpressionEngineAddonUtil;
import com.jeremyworboys.expressionengine.util.dict.ExpressionEngineAddon;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModuleIndex {
    private final Project project;

    public ModuleIndex(@NotNull Project project) {
        this.project = project;
    }

    @NotNull
    public List<ModuleMethod> getMethods() {
        List<ModuleMethod> moduleMethods = new ArrayList<>();
        ExpressionEngineAddonUtil expressionEngineAddonUtil = new ExpressionEngineAddonUtil(project);

        for (ExpressionEngineAddon expressionEngineAddon : expressionEngineAddonUtil.getAddons()) {
            moduleMethods.addAll(getAddonModuleMethods(expressionEngineAddon));
        }

        return moduleMethods;
    }

    @NotNull
    private List<ModuleMethod> getAddonModuleMethods(ExpressionEngineAddon expressionEngineAddon) {
        List<ModuleMethod> methods = new ArrayList<>();
        String moduleName = StringUtil.toLowerCase(expressionEngineAddon.getName());
        PhpClass moduleClass = expressionEngineAddon.getModuleClass();
        boolean hasShortTagMethod = false;

        if (moduleClass != null) {
            // Add all public methods
            for (Method method : moduleClass.getMethods()) {
                if (method.getAccess().isPublic()) {
                    String methodName = StringUtil.toLowerCase(method.getName());
                    if (moduleName.equals(methodName)) {
                        hasShortTagMethod = true;
                        methods.add(new ModuleMethod("exp:" + moduleName, method));
                    }
                    methods.add(new ModuleMethod("exp:" + moduleName + ':' + methodName, method));
                }
            }

            // Add constructor if a method doesn't share the same name as the module
            if (!hasShortTagMethod) {
                Method moduleConstructor = moduleClass.getConstructor();
                if (moduleConstructor != null) {
                    methods.add(new ModuleMethod("exp:" + moduleName, moduleConstructor));
                }
            }
        }

        return methods;
    }
}
