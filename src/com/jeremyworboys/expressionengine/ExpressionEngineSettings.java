package com.jeremyworboys.expressionengine;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(name = "ExpressionEngineSettings")
public class ExpressionEngineSettings implements PersistentStateComponent<ExpressionEngineSettings> {

    public boolean pluginEnabled = false;

    protected Project project;

    public static ExpressionEngineSettings getInstance(Project project) {
        ExpressionEngineSettings settings = ServiceManager.getService(project, ExpressionEngineSettings.class);
        settings.project = project;
        return settings;
    }

    @Override
    public ExpressionEngineSettings getState() {
        return this;
    }

    @Override
    public void loadState(ExpressionEngineSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
