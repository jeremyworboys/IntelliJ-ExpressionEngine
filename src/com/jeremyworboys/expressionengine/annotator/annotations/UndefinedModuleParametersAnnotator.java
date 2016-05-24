package com.jeremyworboys.expressionengine.annotator.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.pattern.ModuleTagPatterns;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModuleOpenTag;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParam;
import com.jeremyworboys.expressionengine.util.module.ModuleIndex;
import com.jeremyworboys.expressionengine.util.module.ModuleMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        ModuleMethod moduleMethod = ModuleIndex.getInstance(project).getModuleMethod(moduleName);
        if (moduleMethod == null) {
            return;
        }

        List<String> moduleParameterNames = moduleMethod.getParameterNames();
        for (String moduleParameterName : moduleParameterNames) {
            if (moduleParameterName.equalsIgnoreCase(paramName)) {
                return;
            }
        }

        // First child is T_PARAM_NAME
        holder.createWarningAnnotation(tagParameter.getFirstChild(), "Unknown Parameter Name");
    }
}
