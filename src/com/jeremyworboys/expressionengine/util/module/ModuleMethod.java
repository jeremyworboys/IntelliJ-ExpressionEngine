package com.jeremyworboys.expressionengine.util.module;

import com.jeremyworboys.expressionengine.util.TagMethodBase;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

public class ModuleMethod extends TagMethodBase {
    public ModuleMethod(@NotNull String tagName, @NotNull PhpClass phpClass, @NotNull Method phpMethod) {
        super(tagName, phpClass, phpMethod);
    }
}
