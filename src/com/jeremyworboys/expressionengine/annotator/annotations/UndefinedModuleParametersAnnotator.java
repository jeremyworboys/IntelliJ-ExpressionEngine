package com.jeremyworboys.expressionengine.annotator.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParam;
import com.jeremyworboys.expressionengine.util.TagMethodBase;
import com.jeremyworboys.expressionengine.util.module.ModuleIndex;
import com.jeremyworboys.expressionengine.util.pattern.ModuleTagPatterns;
import com.jeremyworboys.expressionengine.util.plugin.PluginIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class UndefinedModuleParametersAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Skip all elements aren't tag params of a module
        if (!ModuleTagPatterns.getModuleTagParamPattern().accepts(element)) {
            return;
        }

        ExpressionEngineTagParam tagParameter = (ExpressionEngineTagParam) element;
        ExpressionEngineModuleOpenTag moduleOpenTag = tagParameter.getModuleOpenTag();
        if (moduleOpenTag == null) {
            return;
        }

        String paramName = tagParameter.getTagParamName();
        String moduleName = moduleOpenTag.getModuleName();

        Project project = element.getProject();
        TagMethodBase tagMethod = ModuleIndex.getInstance(project).getMethod(moduleName);
        if (tagMethod == null) {
            tagMethod = PluginIndex.getInstance(project).getMethod(moduleName);
            if (tagMethod == null) {
                return;
            }
        }

        Collection<String> tagParameterNames = tagMethod.getParameterNames();
        for (String tagParameterName : tagParameterNames) {
            if (tagParameterName.equalsIgnoreCase(paramName)) {
                return;
            }
        }

        // First child is T_PARAM_NAME
        holder.createWarningAnnotation(tagParameter.getFirstChild(), "Unknown Parameter Name");
    }
}
