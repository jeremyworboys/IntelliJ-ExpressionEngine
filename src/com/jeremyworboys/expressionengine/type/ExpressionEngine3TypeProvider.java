package com.jeremyworboys.expressionengine.type;

import com.intellij.openapi.project.Project;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.NotNull;
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
        if (ExpressionEngineProjectComponent.isEnabledVersion3(psiElement)) {
            if (getStaticFactoryPattern().accepts(psiElement)) {
                FunctionReference functionElement = (FunctionReference) psiElement;
                PsiElement[] functionParameters = functionElement.getParameters();
                if (functionParameters.length == 1) {
                    return ((StringLiteralExpression) functionParameters[0]).getContents();
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

    private static ElementPattern<PsiElement> getStaticFactoryPattern() {
        return PlatformPatterns
            .psiElement(PhpElementTypes.FUNCTION_CALL)
            .with(functionNameMatchingEE())
            .withChild(firstParameterIsAString());
    }

    @NotNull
    private static PatternCondition<PsiElement> functionNameMatchingEE() {
        return new PatternCondition<PsiElement>("withName") {
            @Override
            public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
                String elementName = ((FunctionReference) psiElement).getName();
                assert elementName != null;
                return elementName.equals("ee");
            }
        };
    }

    private static PsiElementPattern.Capture<PsiElement> firstParameterIsAString() {
        return PlatformPatterns
            .psiElement(PhpElementTypes.PARAMETER_LIST)
            .withFirstChild(PlatformPatterns
                .psiElement(PhpElementTypes.STRING)
            );
    }
}
