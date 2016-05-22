package com.jeremyworboys.expressionengine.referencing;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTagParamValue;
import com.jeremyworboys.expressionengine.util.ExpressionEngineUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TemplateReference implements PsiPolyVariantReference {
    private final ExpressionEngineTagParamValue element;
    private final String paramValue;
    private final TextRange textRange;

    public TemplateReference(@NotNull ExpressionEngineTagParamValue element, @NotNull String paramValue, @NotNull TextRange textRange) {
        this.element = element;
        this.paramValue = paramValue;
        this.textRange = textRange;
    }

    @NotNull
    @Override
    public PsiElement getElement() {
        return element;
    }

    @NotNull
    @Override
    public TextRange getRangeInElement() {
        return textRange;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = element.getProject();
        String templatePath = ExpressionEngineUtil.toTemplatePath(paramValue);
        List<ExpressionEngineFile> templateFiles = ExpressionEngineUtil.getTemplateFiles(project, templatePath);
        List<ResolveResult> results = new ArrayList<ResolveResult>();
        for (ExpressionEngineFile templateFile : templateFiles) {
            results.add(new PsiElementResolveResult(templateFile));
        }
        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        if (resolveResults.length == 1) {
            return resolveResults[0].getElement();
        }
        return null;
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return "";
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        ResolveResult[] results = multiResolve(false);
        for (ResolveResult result : results) {
            PsiElement resultElement = result.getElement();
            if (resultElement != null && resultElement.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public boolean isSoft() {
        return false;
    }
}
