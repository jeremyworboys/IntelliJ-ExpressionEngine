package com.jeremyworboys.expressionengine.container.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SerializableService implements ServiceSerializable {
    private String id;
    private String className;

    public SerializableService(@NotNull String id) {
        this.id = id;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
