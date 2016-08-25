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
        if (!FileUtil.isAbsolute(this.path)) {
            return VfsUtil.findRelativeFile(this.path, project.getBaseDir()) != null;
        }

        return new File(this.path).exists();
    }

    @Nullable
    public File getFile(Project project) {
        if (!FileUtil.isAbsolute(this.path)) {
            VirtualFile virtualFile = VfsUtil.findRelativeFile(this.path, project.getBaseDir());
            if (virtualFile == null) {
                return null;
            }

            return VfsUtil.virtualToIoFile(virtualFile);
        }

        File file = new File(this.path);
        if (!file.exists()) {
            return null;
        }

        return file;
    }
}
