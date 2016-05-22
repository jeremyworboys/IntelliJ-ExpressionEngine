package com.jeremyworboys.expressionengine.annotator.fix;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineElementFactory;
import com.jeremyworboys.expressionengine.util.ExpressionEngineUtil;
import org.jetbrains.annotations.NotNull;

public class CreateTemplateFix extends BaseIntentionAction {
    private final String templatePath;

    public CreateTemplateFix(String templatePath) {
        this.templatePath = templatePath;
    }

    @NotNull
    @Override
    public String getText() {
        return "Create template";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "ExpressionEngine";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                PsiDirectory targetDirectory = ExpressionEngineUtil.getTemplateDirectory(file);
                if (targetDirectory == null) {
                    HintManager.getInstance().showErrorHint(editor, "Can not find a target dir");
                    return;
                }

                if (templatePath == null) {
                    HintManager.getInstance().showErrorHint(editor, "Invalid template name");
                    return;
                }

                ExpressionEngineElementFactory
                    .createAndOpenFile(project, targetDirectory.getVirtualFile(), templatePath);
            }
        });
    }
}
