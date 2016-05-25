package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExpressionEngineModuleElement extends PsiElement {
    boolean isPairModule();

    @NotNull
    ExpressionEngineModuleOpenTag getModuleOpenTag();

    @Nullable
    ExpressionEngineModuleCloseTag getModuleCloseTag();
}
