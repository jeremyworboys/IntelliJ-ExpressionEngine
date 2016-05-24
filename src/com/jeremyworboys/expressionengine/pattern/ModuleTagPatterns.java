package com.jeremyworboys.expressionengine.pattern;

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
}
