package com.jeremyworboys.expressionengine.container;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jeremyworboys.expressionengine.ExpressionEngineSettings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContainerHelper {

    public static List<ContainerFile> getContainerFiles(@NotNull Project project) {
        List<ContainerFile> containerFiles = new ArrayList<>();

        // EE2 does not have container files
        if (!ExpressionEngineProjectComponent.isEnabledVersion3(project)) {
            return containerFiles;
        }

        String systemPath = ExpressionEngineSettings.getInstance(project).systemPath;
        VirtualFile systemDirectory = LocalFileSystem.getInstance().findFileByPath(systemPath);

        // Look for app setup file
        String appSetupPath = "ee/EllisLab/ExpressionEngine/app.setup.php";
        VirtualFile appSetupFile = VfsUtil.findRelativeFile(systemDirectory, appSetupPath.split("/"));
        if (appSetupFile != null) {
            containerFiles.add(new ContainerFile(appSetupFile.getPath()/*, ContainerFileIndex.MAIN, ContainerFileIndex.NamespaceType.BUNDLE*/));
        }

        // Look for addon setup files
        String[] addonPaths = {"ee/EllisLab/Addons", "user/addons"};
        for (String addonPath : addonPaths) {
            VirtualFile firstPartyAddonsDirectory = VfsUtil.findRelativeFile(systemDirectory, addonPath.split("/"));
            if (firstPartyAddonsDirectory != null) {
                for (VirtualFile firstPartyAddonDirectory : firstPartyAddonsDirectory.getChildren()) {
                    if (firstPartyAddonDirectory.isDirectory()) {
                        VirtualFile firstPartyAddonSetupFile = VfsUtil.findRelativeFile(firstPartyAddonDirectory, "addon.setup.php");
                        if (firstPartyAddonSetupFile != null) {
                            containerFiles.add(new ContainerFile(firstPartyAddonSetupFile.getPath()/*, ContainerFileIndex.MAIN, ContainerFileIndex.NamespaceType.BUNDLE*/));
                        }
                    }
                }
            }
        }

        return containerFiles;
    }
}
