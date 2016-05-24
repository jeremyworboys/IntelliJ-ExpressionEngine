package com.jeremyworboys.expressionengine.annotator.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.pattern.ModuleTagPatterns;
import org.jetbrains.annotations.NotNull;

public class UndefinedModuleParametersAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Skip all elements that couldn't be a template reference
        if (!ModuleTagPatterns.getModuleOpenTagPattern().accepts(element)) {
            return;
        }

        // TODO: undefined module params (parse module file for $tmpl->fetch_param()?)
    }
}
