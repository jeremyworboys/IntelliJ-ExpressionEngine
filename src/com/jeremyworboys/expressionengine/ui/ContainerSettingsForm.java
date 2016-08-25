package com.jeremyworboys.expressionengine.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ElementProducer;
import com.intellij.util.ui.ListTableModel;
import com.jeremyworboys.expressionengine.container.ContainerFile;
import com.jeremyworboys.expressionengine.ui.util.ListTableUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ContainerSettingsForm implements Configurable {

    private final Project project;

    private JPanel mainPanel;
    private JPanel tableViewPanel;
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
        this.tableView = new TableView<>();
        this.tableViewModel = new ListTableModel<>(
            new ListTableUtil.PathColumn(project)
        );

        this.attachItems();
        this.tableView.setModelAndUpdateColumns(this.tableViewModel);

        ToolbarDecorator tablePanel = ToolbarDecorator.createDecorator(this.tableView);

        tablePanel.disableAddAction();
        tablePanel.disableRemoveAction();
        tablePanel.disableUpDownActions();

        this.tableViewPanel.add(tablePanel.createPanel());

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

    private void attachItems() {
        // TODO: Implement attachItems() method...
    }
}
