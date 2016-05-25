package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExpressionEngineVariableElement extends PsiElement {
    boolean isPairVariable();

    @NotNull
    ExpressionEngineVariableOpenTag getVariableOpenTag();

    @Nullable
    ExpressionEngineVariableCloseTag getVariableCloseTag();
}
