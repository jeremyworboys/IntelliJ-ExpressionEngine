package com.jeremyworboys.expressionengine.container;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.stubs.indexes.ServicesDefinitionStubIndex;
import com.jeremyworboys.expressionengine.util.ExpressionEngine3InterfacesUtil;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jeremyworboys.expressionengine.util.PhpTypeProviderUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ContainerTypeProvider implements PhpTypeProvider2 {

    final public static char TRIM_KEY = '\u0209';

    @Override
    public char getKey() {
        return '\u0208';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement) {
        if (!PhpTypeProviderUtil.shouldProvideTypeCompletion3(psiElement)) {
            return null;
        }

        // Calls to ee() with first parameter as a string
        if (psiElement instanceof FunctionReference && "ee".equals(((FunctionReference) psiElement).getName())) {
            PsiElement[] parameters = ((FunctionReference) psiElement).getParameters();
            if (parameters.length > 0 && parameters[0] instanceof StringLiteralExpression) {
                String contents = PhpElementsUtil.getStringValue(parameters[0]);
                if (StringUtil.isNotEmpty(contents)) {
                    return ((FunctionReference) psiElement).getSignature() + TRIM_KEY + contents;
                }
            }
        }

        // Calls to a method make() with first parameter as a string
        if (PhpElementsUtil.isMethodWithFirstStringOrFieldReference("make").accepts(psiElement)) {
            return PhpTypeProviderUtil.getReferenceSignature((MethodReference) psiElement, TRIM_KEY);
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String signature, Project project) {

        int endIndex = signature.lastIndexOf(TRIM_KEY);
        if (endIndex == -1) {
            return Collections.emptySet();
        }

        String originalSignature = signature.substring(0, endIndex);
        String parameter = signature.substring(endIndex + 1);

        // Map element signatures to Psi Elements
        PhpIndex phpIndex = PhpIndex.getInstance(project);
        Collection<? extends PhpNamedElement> phpNamedElementCollections = PhpTypeProviderUtil.getTypeSignature(phpIndex, originalSignature);

        if (phpNamedElementCollections.size() == 0) {
            return Collections.emptySet();
        }

        // Skip signatures that aren't method or function calls
        PhpNamedElement phpNamedElement = phpNamedElementCollections.iterator().next();
        if (!(phpNamedElement instanceof Function)) {
            return phpNamedElementCollections;
        }

        // Skip methods that aren't calls to the model factory
        if (phpNamedElement instanceof Method
            && !ExpressionEngine3InterfacesUtil.isContainerGetCall((Method) phpNamedElement)) {
            return phpNamedElementCollections;
        }

        // Resolve parameter to a string (could be a const or property)
        parameter = PhpTypeProviderUtil.getResolvedParameter(phpIndex, parameter);
        if (parameter == null) {
            return phpNamedElementCollections;
        }

        // Find services in index matching the parameter
        FileBasedIndex fileBasedIndex = FileBasedIndex.getInstance();
        List<ServiceSerializable> services = fileBasedIndex.getValues(ServicesDefinitionStubIndex.KEY, parameter, GlobalSearchScope.projectScope(project));

        if (services.size() == 0) {
            return phpNamedElementCollections;
        }

        // Map found model definitions to their FQN
        Collection<PhpNamedElement> phpClasses = new ArrayList<>();
        for (ServiceSerializable service : services) {
            phpClasses.addAll(phpIndex.getClassesByFQN(service.getClassName()));
        }

        return phpClasses;
    }
}
