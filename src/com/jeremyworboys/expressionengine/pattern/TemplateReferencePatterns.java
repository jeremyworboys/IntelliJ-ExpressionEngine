package com.jeremyworboys.expressionengine.pattern;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;

public class TemplateReferencePatterns {
    @NotNull
    public static ElementPattern<PsiElement> getTemplateReferencePattern() {
        //noinspection unchecked
        return PlatformPatterns.or(
            getEmbedTemplateReferencePattern(),
            getLayoutTemplateReferencePattern(),
            getStylesheetTemplateReferencePattern(),
            getPathTemplateReferencePattern(),
            getRouteTemplateReferencePattern(),
            getRedirectTemplateReferencePattern()
        );
    }

    @NotNull
    public static ElementPattern<PsiElement> getEmbedTemplateReferencePattern() {
        return getTemplateReferencePattern(PlatformPatterns.psiElement(ExpressionEngineTypes.T_EMBED));
    }

    @NotNull
    public static ElementPattern<PsiElement> getLayoutTemplateReferencePattern() {
        return getTemplateReferencePattern(PlatformPatterns.psiElement(ExpressionEngineTypes.T_LAYOUT));
    }

    @NotNull
    public static ElementPattern<PsiElement> getStylesheetTemplateReferencePattern() {
        return getTemplateReferencePattern(PlatformPatterns.psiElement(ExpressionEngineTypes.T_STYLESHEET));
    }

    @NotNull
    public static ElementPattern<PsiElement> getPathTemplateReferencePattern() {
        return getTemplateReferencePattern(PlatformPatterns.psiElement(ExpressionEngineTypes.T_PATH));
    }

    @NotNull
    public static ElementPattern<PsiElement> getRouteTemplateReferencePattern() {
        return getTemplateReferencePattern(PlatformPatterns.psiElement(ExpressionEngineTypes.T_ROUTE));
    }

    @NotNull
    public static ElementPattern<PsiElement> getRedirectTemplateReferencePattern() {
        return getTemplateReferencePattern(PlatformPatterns.psiElement(ExpressionEngineTypes.T_REDIRECT));
    }

    @NotNull
    private static ElementPattern<PsiElement> getTemplateReferencePattern(ElementPattern<PsiElement> afterPattern) {
        //noinspection unchecked
        ElementPattern<PsiElement> skippingPattern = PlatformPatterns.or(
            PlatformPatterns.psiElement(ExpressionEngineTypes.T_EQUAL),
            PlatformPatterns.psiElement(ExpressionEngineTypes.T_STRING_START)
        );

        //noinspection unchecked
        return PlatformPatterns.or(
            PlatformPatterns
                .psiElement(ExpressionEngineTypes.PATH_LITERAL)
                .withParent(PlatformPatterns.psiElement(ExpressionEngineTypes.TAG_PARAM_VALUE))
                .afterLeafSkipping(skippingPattern, afterPattern),
            PlatformPatterns
                .psiElement(ExpressionEngineTypes.T_STRING_CONTENT)
                .withSuperParent(2, PlatformPatterns.psiElement(ExpressionEngineTypes.TAG_PARAM_VALUE))
                .afterLeafSkipping(skippingPattern, afterPattern)
        );
    }
}
