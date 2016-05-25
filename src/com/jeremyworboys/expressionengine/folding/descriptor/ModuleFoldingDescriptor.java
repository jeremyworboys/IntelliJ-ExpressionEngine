package com.jeremyworboys.expressionengine.folding.descriptor;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModuleFoldingDescriptor extends FoldingDescriptor {
    @NotNull
    private final ExpressionEngineModule element;

    public ModuleFoldingDescriptor(@NotNull ExpressionEngineModule element, @Nullable FoldingGroup group) {
        super(element.getNode(), getTextRange(element), group);
        this.element = element;
    }

    @NotNull
    private static TextRange getTextRange(@NotNull ExpressionEngineModule element) {
        return new TextRange(element.getTextRange().getStartOffset() + 1, element.getTextRange().getEndOffset() - 1);
    }

    @Nullable
    @Override
    public String getPlaceholderText() {
        return element.getModuleOpenTag().getModuleName();
    }
}
