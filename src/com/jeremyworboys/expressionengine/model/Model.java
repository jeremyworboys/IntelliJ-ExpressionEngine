package com.jeremyworboys.expressionengine.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Model {

    @NotNull
    String getId();

    @NotNull
    String getPrefix();

    @Nullable
    String getClassName();
}
