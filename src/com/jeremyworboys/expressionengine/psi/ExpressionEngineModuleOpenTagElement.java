package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface ExpressionEngineModuleOpenTagElement extends PsiElement {
    @NotNull
    String getModuleName();
}
