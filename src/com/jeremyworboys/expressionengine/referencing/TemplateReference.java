package com.jeremyworboys.expressionengine.referencing;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TemplateReference extends PsiReferenceBase.Poly<PsiElement> {
    @NotNull
    private final ExpressionEngineTagParamValue element;

    public TemplateReference(@NotNull ExpressionEngineTagParamValue element, @NotNull TextRange textRange) {
        super(element, textRange, false);
        this.element = element;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = element.getProject();
        String templatePath = ExpressionEngineUtil.toTemplatePath(getValue());

        List<ResolveResult> results = new ArrayList<ResolveResult>();
        List<ExpressionEngineFile> templateFiles = ExpressionEngineUtil.getTemplateFiles(project, templatePath);
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
        Project project = element.getProject();

        List<LookupElement> variants = new ArrayList<LookupElement>();
        List<ExpressionEngineFile> templateFiles = ExpressionEngineUtil.getTemplateFiles(project);
        for (ExpressionEngineFile templateFile : templateFiles) {
            variants.add(new TemplateReferenceLookupElement(templateFile));
        }

        return variants.toArray();
    }
}
