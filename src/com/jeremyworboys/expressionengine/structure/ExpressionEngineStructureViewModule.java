package com.jeremyworboys.expressionengine.structure;

import com.intellij.util.PlatformIcons;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModule;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExpressionEngineStructureViewModule extends ExpressionEngineStructureViewElement {
    private final ExpressionEngineModule element;

    public ExpressionEngineStructureViewModule(ExpressionEngineModule element) {
        super(element);
        this.element = element;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        return "{" + element.getModuleOpenTag().getModuleName() + "}";
    }

    @Override
    public Icon getIcon(boolean open) {
        return PlatformIcons.METHOD_ICON;
    }
}
