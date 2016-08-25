package com.jeremyworboys.expressionengine;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.util.SettingsUtil;
import org.jetbrains.annotations.Nullable;

public class ExpressionEngineProjectComponent extends AbstractProjectComponent {
    protected ExpressionEngineProjectComponent(Project project) {
        super(project);
    }

    public static boolean isEnabled(Project project) {
        return ExpressionEngineSettings.getInstance(project).pluginEnabled;
    }

    public static boolean isEnabled(@Nullable PsiElement psiElement) {
        return psiElement != null && isEnabled(psiElement.getProject());
    }

    public static boolean isEnabledVersion2(Project project) {
        ExpressionEngineSettings settings = ExpressionEngineSettings.getInstance(project);
        return settings.pluginEnabled
            && SettingsUtil.isSystemPathValidVersion2(settings.systemPath);
    }

    public static boolean isEnabledVersion2(@Nullable PsiElement psiElement) {
        return psiElement != null && isEnabledVersion2(psiElement.getProject());
    }

    public static boolean isEnabledVersion3(Project project) {
        ExpressionEngineSettings settings = ExpressionEngineSettings.getInstance(project);
        return settings.pluginEnabled
            && SettingsUtil.isSystemPathValidVersion3(settings.systemPath);
    }

    public static boolean isEnabledVersion3(@Nullable PsiElement psiElement) {
        return psiElement != null && isEnabledVersion3(psiElement.getProject());
    }

    // TODO: Offer integration setup (see: WordPress plugin)
}
