package com.jeremyworboys.expressionengine.model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.stubs.indexes.ModelsDefinitionStubIndex;
import com.jeremyworboys.expressionengine.util.ExpressionEngine3InterfacesUtil;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jeremyworboys.expressionengine.util.PhpTypeProviderUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ModelTypeProvider implements PhpTypeProvider2 {

    final public static char TRIM_KEY = '\u0210';

    @Override
    public char getKey() {
        return '\u0209';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement) {
        if (!PhpTypeProviderUtil.shouldProvideTypeCompletion3(psiElement)) {
            return null;
        }

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

        // Skip signatures that aren't method calls
        PhpNamedElement phpNamedElement = phpNamedElementCollections.iterator().next();
        if (!(phpNamedElement instanceof Method)) {
            return phpNamedElementCollections;
        }

        // Skip methods that aren't calls to the model factory
        if (!ExpressionEngine3InterfacesUtil.isModelFactoryCall((Method) phpNamedElement)) {
            return phpNamedElementCollections;
        }

        // Resolve parameter to a string (could be a const or property)
        parameter = PhpTypeProviderUtil.getResolvedParameter(phpIndex, parameter);
        if (parameter == null) {
            return phpNamedElementCollections;
        }

        // Find models in index matching the parameter
        FileBasedIndex fileBasedIndex = FileBasedIndex.getInstance();
        List<ModelSerializable> models = fileBasedIndex.getValues(ModelsDefinitionStubIndex.KEY, parameter, GlobalSearchScope.projectScope(project));

        if (models.size() == 0) {
            return phpNamedElementCollections;
        }

        // Map found model definitions to their FQN
        Collection<PhpNamedElement> phpClasses = new ArrayList<>();
        for (ModelSerializable model : models) {
            if (model != null) {
                phpClasses.addAll(phpIndex.getClassesByFQN(model.getClassName()));
            }
        }

        return phpClasses;
    }
}
