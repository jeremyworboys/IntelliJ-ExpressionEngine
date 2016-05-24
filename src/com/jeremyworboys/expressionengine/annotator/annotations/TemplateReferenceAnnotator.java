package com.jeremyworboys.expressionengine.annotator.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.annotator.fix.CreateTemplateFix;
import com.jeremyworboys.expressionengine.pattern.TemplateReferencePatterns;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.util.ExpressionEngineUtil;
import com.jeremyworboys.expressionengine.util.TemplateFilesFinder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TemplateReferenceAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Skip all elements that couldn't be a template reference
        if (!TemplateReferencePatterns.getTemplateReferencePattern().accepts(element)) {
            return;
        }

        // Ignore special {path=} values
        if (TemplateReferencePatterns.getPathTemplateReferencePattern().accepts(element) && isSpecialPathValue(element.getText())) {
            return;
        }

        // Ignore special {redirect=} values
        if (TemplateReferencePatterns.getRedirectTemplateReferencePattern().accepts(element) && isSpecialRedirectValue(element.getText())) {
            return;
        }

        String extension = "html";
        if (TemplateReferencePatterns.getStylesheetTemplateReferencePattern().accepts(element)) {
            extension = "css";
        }

        String templatePath = ExpressionEngineUtil.toTemplatePath(element.getText(), extension);
        if (templatePath == null) {
            return;
        }

        TemplateFilesFinder finder = new TemplateFilesFinder(element.getProject());
        List<ExpressionEngineFile> templateFiles = finder.getTemplateFilesWithPath(templatePath);
        if (templateFiles.size() > 0) {
            return;
        }

        holder.createWarningAnnotation(element, "Missing Template");

        holder.createWarningAnnotation(element, "Create Template")
            .registerFix(new CreateTemplateFix(templatePath));
    }

    private boolean isSpecialPathValue(String text) {
        return text.equals("site_index") || text.equals("logout");
    }

    private boolean isSpecialRedirectValue(String text) {
        return text.equals("404");
    }
}
