package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.jeremyworboys.expressionengine.ExpressionEngineFileType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;

public class TemplateFileFactory {
    private final Project project;

    public TemplateFileFactory(@NotNull Project project) {
        this.project = project;
    }

    public void createAndOpenFile(@NotNull VirtualFile targetDirectory, @NotNull String fileNameWithPath) {
        createAndOpenFile(targetDirectory, fileNameWithPath, "");
    }

    public void createAndOpenFile(@NotNull VirtualFile targetDirectory, @NotNull String fileNameWithPath, @NotNull String contents) {
        VirtualFile virtualFile = createFile(targetDirectory, fileNameWithPath, contents);

        if (virtualFile != null) {
            new OpenFileDescriptor(project, virtualFile, 0).navigate(true);
        }
    }

    @Nullable
    public VirtualFile createFile(@NotNull VirtualFile targetDirectory, @NotNull String fileNameWithPath) {
        return createFile(targetDirectory, fileNameWithPath, "");
    }

    @Nullable
    public VirtualFile createFile(@NotNull VirtualFile targetDirectory, @NotNull String fileNameWithPath, @NotNull String contents) {
        String[] fileNameSegments = fileNameWithPath.split("/");
        String pathString = StringUtils.join(Arrays.copyOf(fileNameSegments, fileNameSegments.length - 1), "/");

        VirtualFile existingFile = VfsUtil.findRelativeFile(targetDirectory, fileNameSegments);
        if (existingFile != null) {
            return null;
        }

        VirtualFile targetDir;
        try {
            targetDir = VfsUtil.createDirectoryIfMissing(targetDirectory, pathString);
        } catch (IOException ignored) {
            return null;
        }

        final PsiFile file = PsiFileFactory.getInstance(project)
            .createFileFromText(fileNameSegments[fileNameSegments.length - 1], ExpressionEngineFileType.INSTANCE, contents);

        CodeStyleManager.getInstance(project).reformat(file);

        PsiDirectory directory = PsiManager.getInstance(project).findDirectory(targetDir);
        if (directory == null) {
            return null;
        }

        PsiElement newFile = directory.add(file);
        if (newFile instanceof PsiFile) {
            return ((PsiFile) newFile).getVirtualFile();
        }

        return null;
    }
}
