package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import com.jeremyworboys.expressionengine.ExpressionEngineSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpressionEngineProjectSettingsForm implements Configurable {

    private final ExpressionEngineSettings settings;
    private ExpressionEngineProjectSettingsPanel settingsPanel;

    private JPanel mainPanel;
    private JPanel settingsPlaceholder;
    private JCheckBox enabledCheckbox;

    public ExpressionEngineProjectSettingsForm(@NotNull final Project project) {
        this.settings = ExpressionEngineSettings.getInstance(project);
        this.settingsPanel = new ExpressionEngineProjectSettingsPanel(project);
        this.settingsPlaceholder.add(this.settingsPanel.getMainPanel(), "Center");
        this.enabledCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ExpressionEngineProjectSettingsForm.this.updateSettingsPanelEnabled();
            }
        });
        this.updateSettingsPanelEnabled();
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
        return enabledCheckbox.isSelected() != settings.pluginEnabled
            || settingsPanel.isModified();

    }

    @Override
    public void apply() throws ConfigurationException {
        settings.pluginEnabled = enabledCheckbox.isSelected();
        settingsPanel.apply();
    }

    @Override
    public void reset() {
        enabledCheckbox.setSelected(settings.pluginEnabled);
        settingsPanel.reset();
        this.updateSettingsPanelEnabled();
    }

    @Override
    public void disposeUIResources() {

    }

    protected void updateSettingsPanelEnabled() {
        UIUtil.setEnabled(this.settingsPanel.getMainPanel(), this.enabledCheckbox.isSelected(), true);
    }
}
