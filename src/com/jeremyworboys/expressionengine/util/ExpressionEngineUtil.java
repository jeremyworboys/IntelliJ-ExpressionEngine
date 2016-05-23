package com.jeremyworboys.expressionengine.util;

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEngineUtil {
    private static String templatePathPattern = "(.+/|^)(.+)\\.group/(.+)\\.(html|css)$";

    @Nullable
    public static PsiDirectory getTemplateDirectory(@NotNull PsiFile file) {
        PsiDirectory groupDirectory = file.getParent();
        if (groupDirectory == null || !groupDirectory.getName().endsWith(".group")) {
            return null;
        }

        PsiDirectory templateDirectory = groupDirectory.getParent();
        if (templateDirectory == null) {
            return null;
        }

        return templateDirectory;
    }

    @Nullable
    public static String toTemplatePath(String templateName) {
        return toTemplatePath(templateName, "html");
    }

    @Nullable
    public static String toTemplatePath(String templateName, String extension) {
        String[] parts = templateName.split("\\/");

        if (parts.length == 2) {
            return parts[0] + ".group/" + parts[1] + "." + extension;
        }

        return null;
    }

    @Nullable
    public static String toTemplateName(String templatePath) {
        Pattern pattern = Pattern.compile(templatePathPattern);
        Matcher matcher = pattern.matcher(templatePath);

        if (matcher.matches()) {
            return matcher.group(2) + "/" + matcher.group(3);
        }

        return null;
    }
}
