package com.jeremyworboys.expressionengine.type;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LoadClassTypeProvider implements PhpTypeProvider2 {
    @Override
    public char getKey() {
        return '\u0206';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement) {
        if (psiElement instanceof FunctionReference) {
            FunctionReference functionReference = (FunctionReference) psiElement;

            if ("load_class".equals(functionReference.getName())) {
                PsiElement[] parameters = functionReference.getParameters();
                if (parameters.length > 0 && parameters[0] instanceof StringLiteralExpression) {
                    String contents = PhpElementsUtil.getStringValue(parameters[0]);
                    if (StringUtil.isNotEmpty(contents)) {
                        return contents;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String signature, Project project) {
        Collection<PhpClass> eeClasses = PhpIndex.getInstance(project).getClassesByFQN("EE_" + signature);
        if (eeClasses.size() > 0) {
            return eeClasses;
        }

        Collection<PhpClass> ciClasses = PhpIndex.getInstance(project).getClassesByFQN("CI_" + signature);
        if (ciClasses.size() > 0) {
            return ciClasses;
        }

        return null;
    }
}
