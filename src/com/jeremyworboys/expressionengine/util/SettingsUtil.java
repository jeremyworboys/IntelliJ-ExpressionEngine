package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public class SettingsUtil {
    private SettingsUtil() {
    }

    @Nullable
    public static String validateSystemPath(String path) {
        return isSystemPathValid(path) ? null : "Please specify a valid ExpressionEngine system directory.";
    }

    @Nullable
    public static String validateTemplatesPath(String path) {
        return isTemplatesPathValid(path) ? null : "Please specify a valid ExpressionEngine templates directory.";
    }

    public static boolean isSystemPathValid(String path) {
        if (!StringUtil.isEmptyOrSpaces(path)) {
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
            if (file != null && file.isDirectory()) {
                return isSystemPathValidVersion2(file)
                    || isSystemPathValidVersion3(file);
            }
        }
        return false;
    }

    public static boolean isSystemPathValidVersion2(String path) {
        if (!StringUtil.isEmptyOrSpaces(path)) {
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
            if (file != null && file.isDirectory()) {
                return isSystemPathValidVersion2(file);
            }
        }
        return false;
    }

    public static boolean isSystemPathValidVersion3(String path) {
        if (!StringUtil.isEmptyOrSpaces(path)) {
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
            if (file != null && file.isDirectory()) {
                return isSystemPathValidVersion3(file);
            }
        }
        return false;
    }

    private static boolean isSystemPathValidVersion2(VirtualFile file) {
        return VfsUtil.findRelativeFile(file, "codeigniter") != null
            && VfsUtil.findRelativeFile(file, "expressionengine") != null;
    }

    private static boolean isSystemPathValidVersion3(VirtualFile file) {
        return VfsUtil.findRelativeFile(file, "ee", "EllisLab") != null
            && VfsUtil.findRelativeFile(file, "user") != null;
    }

    private static boolean isTemplatesPathValid(String path) {
        if (!StringUtil.isEmptyOrSpaces(path)) {
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
            return file != null && file.isDirectory();
        }
        return false;
    }
}
