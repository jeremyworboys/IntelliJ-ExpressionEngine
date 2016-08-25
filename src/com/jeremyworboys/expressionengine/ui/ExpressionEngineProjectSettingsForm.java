package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.jeremyworboys.expressionengine.ExpressionEngineSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExpressionEngineProjectSettingsForm implements Configurable {

    private final Project project;

    private JPanel panel;
    private JCheckBox enabled;

    public ExpressionEngineProjectSettingsForm(@NotNull final Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "ExpressionEngine";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return !getSettings().pluginEnabled == enabled.isSelected();
    }

    @Override
    public void apply() throws ConfigurationException {
        getSettings().pluginEnabled = enabled.isSelected();
    }

    @Override
    public void reset() {
        enabled.setSelected(getSettings().pluginEnabled);
    }

    @Override
    public void disposeUIResources() {

    }

    private ExpressionEngineSettings getSettings() {
        return ExpressionEngineSettings.getInstance(project);
    }
}
