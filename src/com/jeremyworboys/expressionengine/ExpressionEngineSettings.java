package com.jeremyworboys.expressionengine;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(name = "ExpressionEngineSettings")
public class ExpressionEngineSettings implements PersistentStateComponent<ExpressionEngineSettings> {

    public boolean pluginEnabled = false;

    @Override
    public ExpressionEngineSettings getState() {
        return this;
    }

    @Override
    public void loadState(ExpressionEngineSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
