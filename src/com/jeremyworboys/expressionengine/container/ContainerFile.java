package com.jeremyworboys.expressionengine.container;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ContainerFile {

    private String path;

    public ContainerFile() {
    }

    public ContainerFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean exists(@NotNull Project project) {
        if (!FileUtil.isAbsolute(path)) {
            return VfsUtil.findRelativeFile(path, project.getBaseDir()) != null;
        }

        return new File(path).exists();
    }

    @Nullable
    public String getRelativePath(Project project) {
        VirtualFile virtualFile = this.getVirtualFile();
        if (virtualFile == null) {
            return null;
        }

        return VfsUtil.getRelativePath(virtualFile, project.getBaseDir(), '/');
    }

    @Nullable
    private VirtualFile getVirtualFile() {
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }

        return VfsUtil.findFileByIoFile(file, true);
    }
}
