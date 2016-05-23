package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.ExpressionEngineFileType;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEngineUtil {
    private static String templatePathPattern = "(.+/|^)(.+)\\.group/(.+)\\.(html|css)$";

    @NotNull
    public static List<ExpressionEngineFile> getTemplateFiles(@NotNull Project project) {
        List<ExpressionEngineFile> result = new ArrayList<>();

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance()
            .getContainingFiles(FileTypeIndex.NAME, ExpressionEngineFileType.INSTANCE, scope);

        for (VirtualFile virtualFile : virtualFiles) {
            if (virtualFile.getPath().matches(templatePathPattern)) {
                ExpressionEngineFile expressionEngineFile =
                    (ExpressionEngineFile) PsiManager.getInstance(project).findFile(virtualFile);
                if (expressionEngineFile != null) {
                    result.add(expressionEngineFile);
                }
            }
        }

        return result;
    }

    @NotNull
    public static List<ExpressionEngineFile> getTemplateFiles(@NotNull Project project, String templatePath) {
        List<ExpressionEngineFile> result = new ArrayList<>();

        if (templatePath != null && templatePath.contains("/")) {
            String[] filenameParts = templatePath.split("/");
            String filename = filenameParts[filenameParts.length - 1];

            GlobalSearchScope scope = GlobalSearchScope.allScope(project);
            Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance()
                .getContainingFiles(FilenameIndex.NAME, filename, scope);

            for (VirtualFile virtualFile : virtualFiles) {
                if (isMatchingTemplateName(virtualFile, templatePath)) {
                    ExpressionEngineFile expressionEngineFile =
                        (ExpressionEngineFile) PsiManager.getInstance(project).findFile(virtualFile);
                    if (expressionEngineFile != null) {
                        result.add(expressionEngineFile);
                    }
                }
            }
        }

        return result;
    }

    private static boolean isMatchingTemplateName(VirtualFile virtualFile, String templatePath) {
        if (templatePath != null) {
            return virtualFile.getPath().endsWith(templatePath);
        }

        return false;
    }

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
