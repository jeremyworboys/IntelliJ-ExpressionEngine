package com.jeremyworboys.expressionengine.type;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingletonTypeProvider implements PhpTypeProvider2 {
    private static final Pattern eeSingletonPattern = Pattern.compile("^ee\\(\\)->([^-]+)$");
    private static final Pattern ciSingletonPattern = Pattern.compile("^get_instance\\(\\)->([^-]+)$");

    @Override
    public char getKey() {
        return '\u0207';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement) {
        if (psiElement instanceof PhpPsiElement) {
            PhpPsiElement phpElement = (PhpPsiElement) psiElement;
            Matcher eeSingletonMatcher = eeSingletonPattern.matcher(phpElement.getText());
            if (eeSingletonMatcher.matches()) {
                return eeSingletonMatcher.group(1);
            }
            Matcher ciSingletonMatcher = ciSingletonPattern.matcher(phpElement.getText());
            if (ciSingletonMatcher.matches()) {
                return ciSingletonMatcher.group(1);
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String signature, Project project) {
        PhpIndex phpIndex = PhpIndex.getInstance(project);

        // Special case for the loader
        if (signature.equals("load")) {
            return getPhpClasses("loader", phpIndex);
        }

        // Special case for the template parser
        if (signature.equals("TMPL")) {
            return getPhpClasses("Template", phpIndex);
        }

        // Special case for the database driver
        if (signature.equals("db")) {
            Collection<PhpClass> dbClasses = new ArrayList<>();
            dbClasses.addAll(getPhpClasses("DB_mysql_driver", phpIndex));
            dbClasses.addAll(getPhpClasses("DB_active_record", phpIndex));
            return dbClasses;
        }

        Collection<PhpClass> phpClasses = getPhpClasses(signature, phpIndex);
        if (phpClasses.size() > 0) {
            return phpClasses;
        }

        return null;
    }

    @NotNull
    private Collection<PhpClass> getPhpClasses(String signature, PhpIndex phpIndex) {
        Collection<PhpClass> eeClasses = phpIndex.getClassesByFQN("EE_" + signature);
        if (eeClasses.size() > 0) {
            return eeClasses;
        }

        Collection<PhpClass> ciClasses = phpIndex.getClassesByFQN("CI_" + signature);
        if (ciClasses.size() > 0) {
            return ciClasses;
        }

        Collection<PhpClass> phpClasses = phpIndex.getClassesByFQN(StringUtil.capitalize(signature));
        if (phpClasses.size() > 0) {
            return phpClasses;
        }

        return new ArrayList<>();
    }
}
