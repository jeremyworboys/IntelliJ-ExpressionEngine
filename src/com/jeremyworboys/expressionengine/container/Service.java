package com.jeremyworboys.expressionengine.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Service {

    @NotNull
    String getId();

    @NotNull
    String getPrefix();

    @Nullable
    String getClassName();
}
