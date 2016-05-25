package com.jeremyworboys.expressionengine.folding.descriptor;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineModule;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParam;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModuleFoldingDescriptor extends FoldingDescriptor {
    @NotNull
    private final ExpressionEngineModule element;

    public ModuleFoldingDescriptor(@NotNull ExpressionEngineModule element) {
        super(element, element.getTextRange());
        this.element = element;
    }

    @Nullable
    @Override
    public String getPlaceholderText() {
        String moduleName = element.getModuleOpenTag().getModuleName();

        List<String> moduleParameters = new ArrayList<>();
        for (ExpressionEngineTagParam param : element.getModuleOpenTag().getTagParamList()) {
            ExpressionEngineTagParamValue tagParamValue = param.getTagParamValue();
            assert tagParamValue != null;
            moduleParameters.add(param.getTagParamName() + "=" + tagParamValue.getText());
        }

        return "{" + moduleName + " " + StringUtil.join(moduleParameters, " ") + " ...}";
    }
}
