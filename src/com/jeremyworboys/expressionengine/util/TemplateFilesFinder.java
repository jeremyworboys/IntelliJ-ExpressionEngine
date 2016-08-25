package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.ExpressionEngineFileType;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TemplateFilesFinder {
    private static final String templatePathPattern = "(.+/|^)(.+)\\.group/(.+)\\.(html|css)$";
    private final Project project;

    public TemplateFilesFinder(@NotNull Project project) {
        this.project = project;
    }

    private static boolean isMatchingTemplateName(VirtualFile virtualFile, String templatePath) {
        return templatePath != null && virtualFile.getPath().endsWith(templatePath);
    }

    @NotNull
    public List<ExpressionEngineFile> getTemplateFiles() {
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
    public List<ExpressionEngineFile> getTemplateFilesWithPath(String templatePath) {
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
}
