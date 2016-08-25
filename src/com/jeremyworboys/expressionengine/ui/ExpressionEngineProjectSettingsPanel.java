package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton.BrowseFolderActionListener;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.jeremyworboys.expressionengine.ExpressionEngineSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ExpressionEngineProjectSettingsPanel extends JPanel {

    private final ExpressionEngineSettings settings;

    private JPanel mainPanel;
    private TextFieldWithBrowseButton systemPathField;

    public ExpressionEngineProjectSettingsPanel(@NotNull final Project project) {
        this.settings = ExpressionEngineSettings.getInstance(project);

        BrowseFolderActionListener<JTextField> browseFolderListener = createBrowseFolderListener(project);
        this.systemPathField.addBrowseFolderListener(project, browseFolderListener, false);
    }

    @NotNull
    public JComponent getMainPanel() {
        return this.mainPanel;
    }

    public boolean isModified() {
        return !Comparing.strEqual(systemPathField.getText(), settings.systemPath);
    }

    public void apply() {
        settings.systemPath = systemPathField.getText();
    }

    public void reset() {
        systemPathField.setText(settings.systemPath);
    }

    @NotNull
    private BrowseFolderActionListener<JTextField> createBrowseFolderListener(@NotNull final Project project) {
        String title = "Select ExpressionEngine system directory";
        String description = "Select ExpressionEngine system root directory";
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        TextComponentAccessor<JTextField> accessor = TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT;

        return new BrowseFolderActionListener<>(title, description, this.systemPathField, project, descriptor, accessor);
    }
}
