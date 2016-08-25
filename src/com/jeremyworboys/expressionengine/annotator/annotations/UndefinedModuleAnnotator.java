package com.jeremyworboys.expressionengine.annotator.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.util.module.ModuleIndex;
import com.jeremyworboys.expressionengine.util.module.ModuleMethod;
import com.jeremyworboys.expressionengine.annotator.annotations.util.ModuleTagPatterns;
import com.jeremyworboys.expressionengine.util.plugin.PluginIndex;
import com.jeremyworboys.expressionengine.util.plugin.PluginMethod;
import org.jetbrains.annotations.NotNull;

public class UndefinedModuleAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Skip all elements aren't module open tags
        if (!ModuleTagPatterns.getModuleOpenTagPattern().accepts(element)) {
            return;
        }

        ExpressionEngineModuleOpenTag moduleOpenTag = (ExpressionEngineModuleOpenTag) element;
        String moduleName = moduleOpenTag.getModuleName();

        Project project = element.getProject();
        ModuleMethod moduleMethod = ModuleIndex.getInstance(project).getMethod(moduleName);
        if (moduleMethod != null) {
            return;
        }
        PluginMethod pluginMethod = PluginIndex.getInstance(project).getMethod(moduleName);
        if (pluginMethod != null) {
            return;
        }

        holder.createWarningAnnotation(moduleOpenTag, "Unknown Module or Plugin");
    }
}
