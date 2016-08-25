package com.jeremyworboys.expressionengine;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class ExpressionEngineProjectComponent extends AbstractProjectComponent {
    protected ExpressionEngineProjectComponent(Project project) {
        super(project);
    }

    public static boolean isEnabled(Project project) {
        return ExpressionEngineSettings.getInstance(project).pluginEnabled;
    }

    @Contract("null -> false")
    public static boolean isEnabled(@Nullable PsiElement psiElement) {
        return psiElement != null && isEnabled(psiElement.getProject());
    }
}
