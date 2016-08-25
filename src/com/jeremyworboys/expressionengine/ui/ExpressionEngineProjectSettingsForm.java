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

    private final ExpressionEngineSettings settings;
    private ExpressionEngineProjectSettingsPanel settingsPanel;

    private JPanel mainPanel;
    private JPanel settingsPlaceholder;
    private JCheckBox enabled;

    public ExpressionEngineProjectSettingsForm(@NotNull final Project project) {
        this.settings = ExpressionEngineSettings.getInstance(project);
        this.settingsPanel = new ExpressionEngineProjectSettingsPanel();
        this.settingsPlaceholder.add(this.settingsPanel.getMainPanel(), "Center");
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
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return !enabled.isSelected() == settings.pluginEnabled;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.pluginEnabled = enabled.isSelected();
    }

    @Override
    public void reset() {
        enabled.setSelected(settings.pluginEnabled);
    }

    @Override
    public void disposeUIResources() {

    }
}
