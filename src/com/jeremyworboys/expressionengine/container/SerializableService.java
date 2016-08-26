package com.jeremyworboys.expressionengine.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SerializableService implements ServiceSerializable {
    private String id;
    private String prefix;
    private String className;

    public SerializableService(@NotNull String id, @NotNull String prefix) {
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
