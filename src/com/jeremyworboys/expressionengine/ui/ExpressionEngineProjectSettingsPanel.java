package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.jeremyworboys.expressionengine.ExpressionEngineSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ExpressionEngineProjectSettingsPanel extends JPanel {

    private final ExpressionEngineSettings settings;

    private JPanel mainPanel;
    private JTextField systemPathField;

    public ExpressionEngineProjectSettingsPanel(@NotNull final Project project) {
        this.settings = ExpressionEngineSettings.getInstance(project);
    }

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
}
