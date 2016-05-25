package com.jeremyworboys.expressionengine.folding.descriptor;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParam;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamValue;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VariableFoldingDescriptor extends FoldingDescriptor {
    @NotNull
    private final ExpressionEngineVariable element;

    public VariableFoldingDescriptor(@NotNull ExpressionEngineVariable element) {
        super(element, element.getTextRange());
        this.element = element;
    }

    @Nullable
    @Override
    public String getPlaceholderText() {
        String variableName = element.getVariableOpenTag().getVariableName();

        List<String> variableParameters = new ArrayList<>();
        for (ExpressionEngineTagParam param : element.getVariableOpenTag().getTagParamList()) {
            ExpressionEngineTagParamValue tagParamValue = param.getTagParamValue();
            assert tagParamValue != null;
            variableParameters.add(param.getTagParamName() + "=" + tagParamValue.getText());
        }

        return "{" + variableName + " " + StringUtil.join(variableParameters, " ") + " ...}";
    }
}
