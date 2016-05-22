package com.jeremyworboys.expressionengine.psi.impl;

import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceService;
import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamValue;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpressionEnginePsiImplUtil {
    @Nullable
    public static PsiReference getReference(ExpressionEngineTagParamValue element) {
        PsiReference[] references = getReferences(element);
        if (references.length > 0) {
            return references[0];
        }
        return null;
    }

    @NotNull
    public static PsiReference[] getReferences(ExpressionEngineTagParamValue element) {
        IElementType type = element.getNode().getFirstChildNode().getElementType();
        if (type != ExpressionEngineTypes.T_PATH || type != ExpressionEngineTypes.STRING_LITERAL) {
            return PsiReference.EMPTY_ARRAY;
        }
        return PsiReferenceService.getService().getContributedReferences(element);
    }
}
