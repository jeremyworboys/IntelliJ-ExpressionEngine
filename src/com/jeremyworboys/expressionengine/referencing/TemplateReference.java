package com.jeremyworboys.expressionengine.referencing;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamValue;
import com.jeremyworboys.expressionengine.util.ExpressionEngineUtil;
import com.jeremyworboys.expressionengine.util.TemplateFilesFinder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TemplateReference extends PsiReferenceBase.Poly<PsiElement> {
    @NotNull
    private final ExpressionEngineTagParamValue element;
    private final TemplateFilesFinder templateFilesFinder;

    public TemplateReference(@NotNull ExpressionEngineTagParamValue element, @NotNull TextRange textRange) {
        super(element, textRange, false);
        this.element = element;
        templateFilesFinder = new TemplateFilesFinder(element.getProject());
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        String templatePath = ExpressionEngineUtil.toTemplatePath(getValue());

        List<ResolveResult> results = new ArrayList<>();
        List<ExpressionEngineFile> templateFiles = templateFilesFinder.getTemplateFilesWithPath(templatePath);
        for (ExpressionEngineFile templateFile : templateFiles) {
            results.add(new PsiElementResolveResult(templateFile));
        }

        return results.toArray(new ResolveResult[results.size()]);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        String templatePath = ExpressionEngineUtil.toTemplatePath(getValue());

        if (templatePath != null) {
            String[] templateParts = templatePath.split("/");
            templateParts[templateParts.length - 1] = newElementName;
            String newTemplateName = ExpressionEngineUtil.toTemplateName(StringUtil.join(templateParts, "/"));
            if (newTemplateName != null) {
                return element.setName(newTemplateName);
            }
        }

        return element;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("Rebind cannot be performed for " + getClass());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        List<LookupElement> variants = new ArrayList<>();
        List<ExpressionEngineFile> templateFiles = templateFilesFinder.getTemplateFiles();
        for (ExpressionEngineFile templateFile : templateFiles) {
            variants.add(new TemplateReferenceLookupElement(templateFile));
        }

        return variants.toArray();
    }
}
