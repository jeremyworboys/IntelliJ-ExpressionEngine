package com.jeremyworboys.expressionengine.container.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Model {

    @NotNull
    String getId();

    @Nullable
    String getClassName();
}
