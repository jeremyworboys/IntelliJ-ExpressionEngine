package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.project.Project;
import com.jeremyworboys.expressionengine.ExpressionEngineSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ExpressionEngineProjectSettingsPanel extends JPanel {

    private final ExpressionEngineSettings settings;

    private JPanel mainPanel;

    public ExpressionEngineProjectSettingsPanel(@NotNull final Project project) {
        this.settings = ExpressionEngineSettings.getInstance(project);
    }

    public JComponent getMainPanel() {
        return this.mainPanel;
    }
}
