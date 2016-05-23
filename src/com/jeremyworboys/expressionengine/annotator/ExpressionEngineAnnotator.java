package com.jeremyworboys.expressionengine.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.pattern.TemplateReferencePatterns;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.util.ExpressionEngineUtil;
import com.jeremyworboys.expressionengine.util.TemplateFilesFinder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpressionEngineAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        annotateTemplateReference(element, holder);
        // TODO: in conditional expression the string "0" is considered TRUE since it is a non-empty string
        // TODO: in conditional expression negation happens after exponentiation (-5 ** 2 == -25 vs (-5) **2 == 25)
        // TODO: right expr after "matches" operator in conditional expression must be a valid regular expression
        // TODO: layout tag must come before any module or plugin tags
        // TODO: {layout:contents} is reserved for the template data (annotate if used in {layout=""} tag)
        // TODO: using {layout:set} in a looping tag pair will populate the value of the layout variable with the last element of the pair
        // TODO: using {redirect} outside of a conditional body (unless it is the only tag) is likely a mistake
        // TODO: multiple {paginate} blocks with different content need to use paginate=inline
        // TODO: missing module params (e.g. channel:entries[disable])
        // TODO: recommended module params (e.g. channel:entries[disable], current_time[format])
        // TODO: undefined module params (parse module file for $tmpl->fetch_param()?)
        // TODO: fixed set of param values (e.g. paginate[paginate])
    }

    private void annotateTemplateReference(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
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
