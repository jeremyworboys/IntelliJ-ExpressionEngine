package com.jeremyworboys.expressionengine.referencing;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamElement;
import com.jeremyworboys.expressionengine.util.module.ModuleIndex;
import com.jeremyworboys.expressionengine.util.module.ModuleMethod;
import com.jeremyworboys.expressionengine.util.plugin.PluginIndex;
import com.jeremyworboys.expressionengine.util.plugin.PluginMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModuleParamNameReference extends PsiReferenceBase<PsiElement> {
    private final ExpressionEngineTagParamElement element;

    public ModuleParamNameReference(ExpressionEngineTagParamElement element, TextRange rangeInElement) {
        super(element, rangeInElement, false);
        this.element = element;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ExpressionEngineModuleOpenTag moduleOpenTag = element.getModuleOpenTag();
        if (moduleOpenTag == null) {
            return null;
        }

        ModuleMethod moduleMethod = ModuleIndex.getInstance(element.getProject()).getMethod(moduleOpenTag.getModuleName());
        if (moduleMethod != null) {
            return moduleMethod.getParameterName(element.getTagParamName());
        }

        PluginMethod pluginMethod = PluginIndex.getInstance(element.getProject()).getMethod(moduleOpenTag.getModuleName());
        if (pluginMethod != null) {
            return pluginMethod.getParameterName(element.getTagParamName());
        }

        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        // TODO: Module param variants
        return new Object[0];
    }
}
