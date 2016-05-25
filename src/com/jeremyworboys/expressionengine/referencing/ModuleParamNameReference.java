package com.jeremyworboys.expressionengine.referencing;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamElement;
import com.jeremyworboys.expressionengine.util.TagMethodBase;
import com.jeremyworboys.expressionengine.util.module.ModuleIndex;
import com.jeremyworboys.expressionengine.util.module.ModuleMethod;
import com.jeremyworboys.expressionengine.util.plugin.PluginIndex;
import com.jeremyworboys.expressionengine.util.plugin.PluginMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

        ModuleMethod moduleMethod = moduleIndex().getMethod(moduleOpenTag.getModuleName());
        if (moduleMethod != null) {
            return moduleMethod.getParameterName(element.getTagParamName());
        }

        PluginMethod pluginMethod = pluginIndex().getMethod(moduleOpenTag.getModuleName());
        if (pluginMethod != null) {
            return pluginMethod.getParameterName(element.getTagParamName());
        }

        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        ExpressionEngineModuleOpenTag moduleOpenTag = element.getModuleOpenTag();
        if (moduleOpenTag == null) {
            return new Object[0];
        }

        TagMethodBase tagMethod = moduleIndex().getMethod(moduleOpenTag.getModuleName());
        if (tagMethod == null) {
            tagMethod = pluginIndex().getMethod(moduleOpenTag.getModuleName());
        }
        if (tagMethod == null) {
            return new Object[0];
        }

        List<LookupElement> variants = new ArrayList<>();
        for (String paramName : tagMethod.getParameterNames()) {
            variants.add(LookupElementBuilder.create(paramName));
        }

        return variants.toArray();
    }

    @NotNull
    private PluginIndex pluginIndex() {
        return PluginIndex.getInstance(element.getProject());
    }

    @NotNull
    private ModuleIndex moduleIndex() {
        return ModuleIndex.getInstance(element.getProject());
    }
}
