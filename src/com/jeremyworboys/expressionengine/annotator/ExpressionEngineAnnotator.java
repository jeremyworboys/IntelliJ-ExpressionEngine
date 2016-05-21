package com.jeremyworboys.expressionengine.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.annotator.fix.CreateTemplateFix;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.util.ExpressionEngineUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpressionEngineAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        annotateTemplateReference(element, holder);
    }

    private void annotateTemplateReference(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!ExpressionEngineUtil.getTemplateFileReferencePattern().accepts(element)) {
            return;
        }

        String templateName = element.getText();
        List<ExpressionEngineFile> templateFiles = ExpressionEngineUtil.getTemplateFiles(element.getProject(), templateName);

        if (templateFiles.size() > 0) {
            return;
        }

        holder.createWarningAnnotation(element, "Missing Template");

        holder.createWarningAnnotation(element, "Create Template")
            .registerFix(new CreateTemplateFix(templateName));
    }
}
