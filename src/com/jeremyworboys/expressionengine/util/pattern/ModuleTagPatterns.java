package com.jeremyworboys.expressionengine.util.pattern;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;

public class ModuleTagPatterns {
    @NotNull
    public static ElementPattern<PsiElement> getModuleOpenTagPattern() {
        return PlatformPatterns.psiElement(ExpressionEngineTypes.MODULE_OPEN_TAG);
    }

    @NotNull
    public static ElementPattern<PsiElement> getModuleTagParamPattern() {
        // TODO: Test this correctly matches conditional tag params
        return PlatformPatterns
            .psiElement(ExpressionEngineTypes.TAG_PARAM)
            .withTreeParent(getModuleOpenTagPattern());
    }
}
