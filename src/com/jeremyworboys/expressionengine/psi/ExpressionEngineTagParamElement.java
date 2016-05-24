package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface ExpressionEngineTagParamElement extends PsiElement {
    String getTagParamName();

    @Nullable
    ExpressionEngineModuleOpenTag getModuleOpenTag();
}
