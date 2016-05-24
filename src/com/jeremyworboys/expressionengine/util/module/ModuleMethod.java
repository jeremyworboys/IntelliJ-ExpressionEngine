package com.jeremyworboys.expressionengine.util.module;

import com.jetbrains.php.lang.psi.elements.Method;

public class ModuleMethod {
    private final String tagName;
    private final Method method;

    public ModuleMethod(String tagName, Method method) {
        this.tagName = tagName;
        this.method = method;
    }
}
