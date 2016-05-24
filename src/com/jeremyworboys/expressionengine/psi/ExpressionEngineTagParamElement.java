package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.PsiNamedElement;

public interface ExpressionEngineTagParamElement extends PsiNamedElement {
    String getTagParamName();
}
