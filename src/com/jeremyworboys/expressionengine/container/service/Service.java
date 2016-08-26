package com.jeremyworboys.expressionengine.container.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Service {

    @NotNull
    String getId();

    @Nullable
    String getClassName();
}
