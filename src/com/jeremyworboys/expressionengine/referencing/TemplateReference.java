package com.jeremyworboys.expressionengine.referencing;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
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
    private final TextRange textRange;
    private final String paramValue;

    public TemplateReference(@NotNull ExpressionEngineTagParamValue element) {
        this.element = element;
        paramValue = StringUtil.stripQuotesAroundValue(element.getText());
        if (paramValue.equals(element.getText())) {
            textRange = new TextRange(0, paramValue.length());
        } else {
            textRange = new TextRange(1, paramValue.length() + 1);
        }
    }

    @Override
    public PsiElement getElement() {
        return element;
    }

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
            if (result.getElement().equals(element)) {
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
