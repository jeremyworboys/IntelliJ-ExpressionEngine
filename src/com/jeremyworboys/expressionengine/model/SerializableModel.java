package com.jeremyworboys.expressionengine.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SerializableModel implements ModelSerializable {
    private String id;
    private String prefix;
    private String className;

    public SerializableModel(@NotNull String id, @NotNull String prefix) {
        this.id = id;
        this.prefix = prefix;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getPrefix() {
        return prefix;
    }

    @Nullable
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
