package com.jeremyworboys.expressionengine.container;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jeremyworboys.expressionengine.ExpressionEngineSettings;
import com.jeremyworboys.expressionengine.container.model.ModelSerializable;
import com.jeremyworboys.expressionengine.container.service.SerializableService;
import com.jeremyworboys.expressionengine.container.service.ServiceSerializable;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.ArrayHashElement;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContainerHelper {

    @NotNull
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

    @NotNull
    public static List<ServiceSerializable> getServicesInFile(PsiFile psiFile) {
        final List<ServiceSerializable> services = new ArrayList<>();

        // TODO: This could be cleaned up and optimized
        // Use sf2 plugin ServiceContainerUtil::getServicesInFile() as a reference
        if (psiFile instanceof PhpFile) {
            ArrayCreationExpression phpArray = PhpElementsUtil.getReturnedArrayFromFile((PhpFile) psiFile);
            if (phpArray != null) {
                String namespacePrefix = "";

                // Loop through *.setup.php to find namespace
                for (ArrayHashElement phpArrayElement : phpArray.getHashElements()) {
                    StringLiteralExpression phpArrayElementKey = (StringLiteralExpression) phpArrayElement.getKey();
                    PsiElement phpArrayElementValue = phpArrayElement.getValue();
                    // Find an entry with key "services" and an array value
                    if (phpArrayElementKey != null
                        && "namespace".equals(phpArrayElementKey.getContents())
                        && phpArrayElementValue instanceof StringLiteralExpression) {
                        StringLiteralExpression namespaceElement = (StringLiteralExpression) phpArrayElementValue;
                        namespacePrefix = StringUtil.trimStart(StringUtil.trimEnd(namespaceElement.getContents(), "\\"), "\\");
                    }
                }

                // Loop through *.setup.php to find services
                for (ArrayHashElement phpArrayElement : phpArray.getHashElements()) {
                    StringLiteralExpression phpArrayElementKey = (StringLiteralExpression) phpArrayElement.getKey();
                    PsiElement phpArrayElementValue = phpArrayElement.getValue();
                    // TODO: Locate "namespace" key and prepend to string services
                    // Find an entry with key "services" and an array value
                    if (phpArrayElementKey != null
                        && "services".equals(phpArrayElementKey.getContents())
                        && phpArrayElementValue instanceof ArrayCreationExpression) {
                        ArrayCreationExpression servicesArray = (ArrayCreationExpression) phpArrayElementValue;
                        // Loop through services array
                        for (ArrayHashElement servicesArrayElement : servicesArray.getHashElements()) {
                            PsiElement serviceKey = servicesArrayElement.getKey();
                            PsiElement serviceValue = servicesArrayElement.getValue();
                            if (serviceKey instanceof StringLiteralExpression && serviceValue != null) {
                                String serviceName = ((StringLiteralExpression) serviceKey).getContents();
                                String serviceClassName = "";

                                // Is service a closure
                                if (serviceValue.getFirstChild() instanceof Function) {
                                    PhpType serviceReturnType = ((Function) serviceValue.getFirstChild()).getLocalType(true);
                                    if (!serviceReturnType.isEmpty()) {
                                        serviceClassName = serviceReturnType.toStringResolved();
                                    }
                                }

                                // Is service a string
                                else if (serviceValue instanceof StringLiteralExpression) {
                                    String rawServiceClassName = ((StringLiteralExpression) serviceValue).getContents();
                                    if (rawServiceClassName.startsWith("\\")) {
                                        serviceClassName = rawServiceClassName;
                                    } else {
                                        serviceClassName = namespacePrefix + "\\" + rawServiceClassName;
                                    }
                                }

                                // Create service entry
                                if (StringUtils.isNotBlank(serviceName) && StringUtils.isNotBlank(serviceClassName)) {
                                    SerializableService serializableService = new SerializableService(serviceName);
                                    serializableService.setClassName(serviceClassName);

                                    services.add(serializableService);
                                }
                            }
                        }
                    }
                }
            }
        }

        return services;
    }

    @NotNull
    public static List<ModelSerializable> getModelsInFile(PsiFile psiFile) {
        final List<ModelSerializable> models = new ArrayList<>();

        // TODO: Implement getModelsInFile() method...

        return models;
    }
}
