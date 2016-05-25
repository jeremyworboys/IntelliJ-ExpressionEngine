package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.jeremyworboys.expressionengine.util.dict.ExpressionEngineAddon;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class TagIndexBase<M extends TagMethodBase> {
    protected final Project project;

    protected TagIndexBase(@NotNull Project project) {
        this.project = project;
    }

    @NotNull
    public Map<String, M> getMethods() {
        Map<String, M> tagMethods = new HashMap<>();
        ExpressionEngineAddonUtil expressionEngineAddonUtil = ExpressionEngineAddonUtil.getInstance(project);

        for (ExpressionEngineAddon expressionEngineAddon : expressionEngineAddonUtil.getAddons()) {
            tagMethods.putAll(getAddonMethods(expressionEngineAddon));
        }

        return tagMethods;
    }

    @Nullable
    public M getMethod(String tagName) {
        return getMethods().get(tagName);
    }

    @NotNull
    private Map<String, M> getAddonMethods(@NotNull ExpressionEngineAddon expressionEngineAddon) {
        Map<String, M> methods = new HashMap<>();
        String tagName = StringUtil.toLowerCase(expressionEngineAddon.getName());
        PhpClass tagClass = getTagClass(expressionEngineAddon);

        if (tagClass != null) {
            // Add all public methods
            for (Method method : tagClass.getMethods()) {
                if (isViableTagMethod(method)) {
                    String methodName = StringUtil.toLowerCase(method.getName());
                    if (methodName.equals(tagName)) {
                        String fullTagName = "exp:" + tagName;
                        methods.put(fullTagName, createTagMethod(fullTagName, tagClass, method));
                    }
                    String fullTagName = "exp:" + tagName + ':' + methodName;
                    methods.put(fullTagName, createTagMethod(fullTagName, tagClass, method));
                }
            }

            // Add constructor if a method doesn't share the same name as the class
            String fullTagName = "exp:" + tagName;
            if (!methods.containsKey(fullTagName)) {
                Method tagConstructor = tagClass.getConstructor();
                if (tagConstructor != null && isViableTagConstructor(tagConstructor)) {
                    methods.put(fullTagName, createTagMethod(fullTagName, tagClass, tagConstructor));
                }
            }
        }

        return methods;
    }

    @Nullable
    protected abstract PhpClass getTagClass(@NotNull ExpressionEngineAddon expressionEngineAddon);

    @NotNull
    protected abstract M createTagMethod(@NotNull String tagName, @NotNull PhpClass tagClass, @NotNull Method method);

    private boolean isViableTagMethod(@NotNull Method method) {
        return method.getAccess().isPublic()
            && !method.getName().startsWith("_")
            && hasOnlyOptionalParameters(method);

    }

    private boolean isViableTagConstructor(@NotNull Method method) {
        return method.getAccess().isPublic()
            && hasOnlyOptionalParameters(method);

    }

    private boolean hasOnlyOptionalParameters(@NotNull Method method) {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            if (!parameter.isOptional()) {
                return false;
            }
        }

        return true;
    }
}
