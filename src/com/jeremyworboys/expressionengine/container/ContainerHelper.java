package com.jeremyworboys.expressionengine.container;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jeremyworboys.expressionengine.model.ModelSerializable;
import com.jeremyworboys.expressionengine.model.SerializableModel;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ContainerHelper {

    @NotNull
    public static List<ServiceSerializable> getServicesInFile(PsiFile psiFile, String servicePrefix) {

        ArrayCreationExpression setupArray = getSetupArray(psiFile);
        if (setupArray == null) {
            return Collections.emptyList();
        }

        final List<ServiceSerializable> services = new ArrayList<>();
        Map<String, String> servicesEntries = findServicesWithKey(setupArray, "services");

        for (Map.Entry<String, String> entry : servicesEntries.entrySet()) {
            SerializableService serializableService = new SerializableService(entry.getKey(), servicePrefix);
            serializableService.setClassName(entry.getValue());

            services.add(serializableService);
        }

        return services;
    }

    // TODO: This doesn't belong here..
    // Most of this class should probably be somewhere else
    @NotNull
    public static List<ModelSerializable> getModelsInFile(PsiFile psiFile, String servicePrefix) {

        ArrayCreationExpression setupArray = getSetupArray(psiFile);
        if (setupArray == null) {
            return Collections.emptyList();
        }

        final List<ModelSerializable> models = new ArrayList<>();
        Map<String, String> modelsEntries = findServicesWithKey(setupArray, "models");

        for (Map.Entry<String, String> entry : modelsEntries.entrySet()) {
            SerializableModel serializableModel = new SerializableModel(entry.getKey(), servicePrefix);
            serializableModel.setClassName(entry.getValue());

            models.add(serializableModel);
        }

        return models;
    }

    @Nullable
    private static ArrayCreationExpression getSetupArray(PsiFile psiFile) {
        if (psiFile instanceof PhpFile) {
            return PhpElementsUtil.getReturnedArrayFromFile((PhpFile) psiFile);
        }
        return null;
    }

    @NotNull
    private static String getNamespacePrefix(ArrayCreationExpression phpArray) {
        PhpPsiElement namespaceValue = PhpElementsUtil.getValueOfKeyInArray(phpArray, "namespace");
        String namespaceValueString = PhpElementsUtil.getStringValue(namespaceValue);

        if (namespaceValueString != null) {
            return namespaceValueString.replaceAll("^\\|\\$", "");
        }

        return "";
    }

    @NotNull
    private static Map<String, String> findServicesWithKey(ArrayCreationExpression setupArray, String setupKey) {
        final Map<String, String> mappings = new HashMap<>();

        String namespacePrefix = getNamespacePrefix(setupArray);
        PhpPsiElement servicesValue = PhpElementsUtil.getValueOfKeyInArray(setupArray, setupKey);

        if (servicesValue instanceof ArrayCreationExpression) {
            ArrayCreationExpression servicesArray = (ArrayCreationExpression) servicesValue;
            // Loop through services array
            for (ArrayHashElement servicesArrayElement : servicesArray.getHashElements()) {
                PsiElement serviceKey = servicesArrayElement.getKey();
                PsiElement serviceValue = servicesArrayElement.getValue();
                if (serviceKey instanceof StringLiteralExpression && serviceValue != null) {
                    String serviceName = PhpElementsUtil.getStringValue(serviceKey);
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
                        String serviceValueString = PhpElementsUtil.getStringValue(servicesValue);
                        if (serviceValueString != null) {
                            serviceClassName = namespacePrefix + "\\" + serviceValueString;
                        }
                    }

                    // Create service entry
                    if (StringUtil.isNotEmpty(serviceName) && StringUtil.isNotEmpty(serviceClassName)) {
                        mappings.put(serviceName, serviceClassName);
                    }
                }
            }
        }

        return mappings;
    }
}
