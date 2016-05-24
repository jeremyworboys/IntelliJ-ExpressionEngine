package com.jeremyworboys.expressionengine.annotator.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.pattern.ModuleTagPatterns;
import org.jetbrains.annotations.NotNull;

public class UndefinedModuleParametersAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Skip all elements aren't tag params of a module
        if (!ModuleTagPatterns.getModuleTagParamPattern().accepts(element)) {
            return;
        }

        // TODO: undefined module params (parse module file for $tmpl->fetch_param()?)
    }
}
