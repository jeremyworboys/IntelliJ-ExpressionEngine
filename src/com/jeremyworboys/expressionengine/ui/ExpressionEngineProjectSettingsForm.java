package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton.BrowseFolderActionListener;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
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

    private JPanel mainPanel;
    private JPanel settingsPanel;
    private JCheckBox enabledCheckbox;
    private TextFieldWithBrowseButton systemPathField;

    public ExpressionEngineProjectSettingsForm(@NotNull final Project project) {
        this.settings = ExpressionEngineSettings.getInstance(project);

        BrowseFolderActionListener<JTextField> browseFolderListener = createBrowseFolderListener(project);
        this.systemPathField.addBrowseFolderListener(project, browseFolderListener, false);

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
            || !Comparing.strEqual(systemPathField.getText(), settings.systemPath);

    }

    @Override
    public void apply() throws ConfigurationException {
        settings.pluginEnabled = enabledCheckbox.isSelected();
        settings.systemPath = systemPathField.getText();
    }

    @Override
    public void reset() {
        enabledCheckbox.setSelected(settings.pluginEnabled);
        systemPathField.setText(settings.systemPath);
        this.updateSettingsPanelEnabled();
    }

    @Override
    public void disposeUIResources() {

    }

    @NotNull
    private BrowseFolderActionListener<JTextField> createBrowseFolderListener(@NotNull final Project project) {
        String title = "Select ExpressionEngine system directory";
        String description = "Select ExpressionEngine system root directory";
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        TextComponentAccessor<JTextField> accessor = TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT;

        return new BrowseFolderActionListener<>(title, description, this.systemPathField, project, descriptor, accessor);
    }

    private void updateSettingsPanelEnabled() {
        UIUtil.setEnabled(this.settingsPanel, this.enabledCheckbox.isSelected(), true);
    }
}
