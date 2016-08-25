package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.ListTableModel;
import com.jeremyworboys.expressionengine.container.ContainerFile;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ContainerSettingsForm implements Configurable {

    private final Project project;

    private JPanel mainPanel;
    private JPanel listViewPanel;
    private JButton resetButton;

    private TableView<ContainerFile> tableView;
    private ListTableModel<ContainerFile> tableViewModel;

    public ContainerSettingsForm(@NotNull Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Container";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        // TODO: Implement createComponent() method...
        return this.mainPanel;
    }

    @Override
    public boolean isModified() {
        // TODO: Implement isModified() method...
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        // TODO: Implement apply() method...

    }

    @Override
    public void reset() {
        // TODO: Implement reset() method...

    }

    @Override
    public void disposeUIResources() {

    }
}
