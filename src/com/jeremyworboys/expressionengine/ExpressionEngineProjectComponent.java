package com.jeremyworboys.expressionengine;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpressionEngineProjectComponent implements ProjectComponent {
    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "ExpressionEngineProjectComponent";
    }

    public static boolean isEnabled(Project project) {
        return ExpressionEngineSettings.getInstance(project).pluginEnabled;
    }

    @Contract("null -> false")
    public static boolean isEnabled(@Nullable PsiElement psiElement) {
        return psiElement != null && isEnabled(psiElement.getProject());
    }
}
