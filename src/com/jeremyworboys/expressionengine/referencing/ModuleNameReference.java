package com.jeremyworboys.expressionengine.referencing;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleNameReference extends PsiReferenceBase<PsiElement> {
    private final ExpressionEngineModuleOpenTag element;

    public ModuleNameReference(ExpressionEngineModuleOpenTag element, TextRange rangeInElement) {
        super(element, rangeInElement, false);
        this.element = element;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ModuleMethod moduleMethod = moduleIndex().getMethod(element.getModuleName());
        if (moduleMethod != null) {
            return moduleMethod.getPhpMethod();
        }

        PluginMethod pluginMethod = pluginIndex().getMethod(element.getModuleName());
        if (pluginMethod != null) {
            return pluginMethod.getPhpMethod();
        }

        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        List<LookupElement> variants = new ArrayList<>();

        Map<String, ModuleMethod> moduleMethods = moduleIndex().getMethods();
        for (String tagName : moduleMethods.keySet()) {
            variants.add(LookupElementBuilder.create(tagName));
        }

        Map<String, PluginMethod> pluginMethods = pluginIndex().getMethods();
        for (String tagName : pluginMethods.keySet()) {
            variants.add(LookupElementBuilder.create(tagName));
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
