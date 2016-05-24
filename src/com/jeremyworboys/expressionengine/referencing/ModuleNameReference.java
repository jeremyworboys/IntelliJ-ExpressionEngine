package com.jeremyworboys.expressionengine.referencing;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.util.module.ModuleIndex;
import com.jeremyworboys.expressionengine.util.module.ModuleMethod;
import com.jeremyworboys.expressionengine.util.plugin.PluginIndex;
import com.jeremyworboys.expressionengine.util.plugin.PluginMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModuleNameReference extends PsiReferenceBase<PsiElement> {
    private final ExpressionEngineModuleOpenTag element;

    public ModuleNameReference(ExpressionEngineModuleOpenTag element, TextRange rangeInElement) {
        super(element, rangeInElement, false);
        this.element = element;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ModuleMethod moduleMethod = ModuleIndex.getInstance(element.getProject()).getMethod(element.getModuleName());
        if (moduleMethod != null) {
            return moduleMethod.getPhpMethod();
        }

        PluginMethod pluginMethod = PluginIndex.getInstance(element.getProject()).getMethod(element.getModuleName());
        if (pluginMethod != null) {
            return pluginMethod.getPhpMethod();
        }

        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        // TODO: Module method variants
        return new Object[0];
    }
}
