package com.jeremyworboys.expressionengine.type;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ExpressionEngine3TypeProvider implements PhpTypeProvider2 {
    @Override
    public char getKey() {
        return '\u0208';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement) {
        // Don't lookup if in low-power mode or isn't a configured ee3 project
        if (DumbService.getInstance(psiElement.getProject()).isDumb()
            || !ExpressionEngineProjectComponent.isEnabledVersion3(psiElement)) {
            return null;
        }

        if (psiElement instanceof FunctionReference){
            FunctionReference functionElement = (FunctionReference) psiElement;
            if ("ee".equals(functionElement.getName())) {
                PsiElement[] functionParameters = functionElement.getParameters();
                if (functionParameters.length > 1 && functionParameters[0] instanceof StringLiteralExpression) {
                    String serviceParameter = ((StringLiteralExpression) functionParameters[0]).getContents();
                    if (StringUtils.isNotBlank(serviceParameter)) {
                        return serviceParameter;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String signature, Project project) {
        // TODO: Build index of services and lookup class name
        return null;
    }
}
