package com.jeremyworboys.expressionengine.ui;

import com.intellij.facet.impl.ui.FacetErrorPanel;
import com.intellij.facet.ui.FacetEditorValidator;
import com.intellij.facet.ui.ValidationResult;
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
import com.jeremyworboys.expressionengine.util.SettingsUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExpressionEngineProjectSettingsForm implements Configurable {

    private final ExpressionEngineSettings settings;

    private JPanel mainPanel;
    private JPanel settingsPanel;
    private JPanel errorPanel;
    private JCheckBox enabledCheckbox;
    private TextFieldWithBrowseButton systemPathField;
    private TextFieldWithBrowseButton templatesPathField;
    private FacetErrorPanel facetErrorPanel;

    public ExpressionEngineProjectSettingsForm(@NotNull final Project project) {
        this.settings = ExpressionEngineSettings.getInstance(project);

        // Setup enabled checkbox
        this.enabledCheckbox.addActionListener(e -> ExpressionEngineProjectSettingsForm.this.updateSettingsPanelEnabled());
        this.updateSettingsPanelEnabled();

        // Setup path browser
        this.systemPathField.addBrowseFolderListener(project, createBrowseFolderListener(project), false);
        this.templatesPathField.addBrowseFolderListener(project, createBrowseFolderListener(project), false);

        // Setup validation
        this.facetErrorPanel = new FacetErrorPanel();
        this.facetErrorPanel.getValidatorsManager().registerValidator(this.createSystemPathValidator(), this.systemPathField.getTextField());
        this.facetErrorPanel.getValidatorsManager().registerValidator(this.createTemplatesPathValidator(), this.templatesPathField.getTextField());
        this.facetErrorPanel.getValidatorsManager().validate();
        this.errorPanel.add(this.facetErrorPanel.getComponent());
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

    @NotNull
    private FacetEditorValidator createSystemPathValidator() {
        return new FacetEditorValidator() {
            @NotNull
            public ValidationResult check() {
                String result = ExpressionEngineProjectSettingsForm.this.getSystemPathValidationMessage();
                if (result != null) {
                    return new ValidationResult(result);
                } else {
                    return ValidationResult.OK;
                }
            }
        };
    }

    @NotNull
    private FacetEditorValidator createTemplatesPathValidator() {
        return new FacetEditorValidator() {
            @NotNull
            public ValidationResult check() {
                String result = ExpressionEngineProjectSettingsForm.this.getTemplatesPathValidationMessage();
                if (result != null) {
                    return new ValidationResult(result);
                } else {
                    return ValidationResult.OK;
                }
            }
        };
    }

    @Nullable
    private String getSystemPathValidationMessage() {
        if (!this.enabledCheckbox.isSelected()) {
            return null;
        } else {
            String result = SettingsUtil.validateSystemPath(this.systemPathField.getText());
            return result != null ? result : null;
        }
    }

    @Nullable
    private String getTemplatesPathValidationMessage() {
        if (!this.enabledCheckbox.isSelected()) {
            return null;
        } else {
            String result = SettingsUtil.validateTemplatesPath(this.templatesPathField.getText());
            return result != null ? result : null;
        }
    }

    private void updateSettingsPanelEnabled() {
        UIUtil.setEnabled(this.settingsPanel, this.enabledCheckbox.isSelected(), true);

        if (this.facetErrorPanel != null) {
            this.facetErrorPanel.getValidatorsManager().validate();
        }
    }
}
