package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface ExpressionEngineVariableOpenTagElement extends PsiElement {
    @NotNull
    String getVariableName();
}
