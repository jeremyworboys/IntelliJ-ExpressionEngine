package com.jeremyworboys.expressionengine.referencing;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.jeremyworboys.expressionengine.icons.ExpressionEngineIcons;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.util.ExpressionEngineUtil;
import org.jetbrains.annotations.NotNull;

public class TemplateReferenceLookupElement extends LookupElement {
    private final ExpressionEngineFile templateFile;

    public TemplateReferenceLookupElement(ExpressionEngineFile templateFile) {
        this.templateFile = templateFile;
    }

    @NotNull
    @Override
    public String getLookupString() {
        String templateName = ExpressionEngineUtil.toTemplateName(templateFile.getVirtualFile().getPath());
        return templateName != null ? templateName : templateFile.getName();
    }

    public void renderElement(LookupElementPresentation presentation) {
        presentation.setItemText(getLookupString());
        presentation.setTypeText(templateFile.getName());
        presentation.setTypeGrayed(true);
        presentation.setIcon(ExpressionEngineIcons.FILE);
    }
}
