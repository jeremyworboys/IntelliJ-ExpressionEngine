package com.jeremyworboys.expressionengine.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.annotator.annotations.TemplateReferenceAnnotator;
import com.jeremyworboys.expressionengine.annotator.annotations.UndefinedModuleAnnotator;
import com.jeremyworboys.expressionengine.annotator.annotations.UndefinedModuleParametersAnnotator;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        new TemplateReferenceAnnotator().annotate(element, holder);
        new UndefinedModuleAnnotator().annotate(element, holder);
        new UndefinedModuleParametersAnnotator().annotate(element, holder);
        // TODO: constant expression in conditional (e.g. {if TRUE} ... {endif})
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
        // TODO: fixed set of param values (e.g. paginate[paginate])
    }
}
