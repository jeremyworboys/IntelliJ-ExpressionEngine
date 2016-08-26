package com.jeremyworboys.expressionengine.container.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SerializableModel implements ModelSerializable {
    private String id;
    private String className;

    public SerializableModel(@NotNull String id) {
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
