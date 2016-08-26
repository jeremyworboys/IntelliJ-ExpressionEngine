package com.jeremyworboys.expressionengine.container;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jeremyworboys.expressionengine.util.PhpElementsUtil;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ModelTypeProvider implements PhpTypeProvider2 {
    @Override
    public char getKey() {
        return '\u0209';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement) {
        // Don't lookup if in low-power mode or isn't a configured ee3 project
        if (DumbService.getInstance(psiElement.getProject()).isDumb()
            || !ExpressionEngineProjectComponent.isEnabledVersion3(psiElement)) {
            return null;
        }

        if (PhpElementsUtil.isMethodWithFirstStringOrFieldReference("get", "make").accepts(psiElement)) {
            // TODO: Extract the below into a util method, it's used in most type providers and doesn't yet handle references
            // See sf2 plugin PhpTypeProviderUtil::getReferenceSignature()
            PsiElement[] functionParameters = ((MethodReference) psiElement).getParameters();
            if (functionParameters.length > 0 && functionParameters[0] instanceof StringLiteralExpression) {
                String serviceParameter = ((StringLiteralExpression) functionParameters[0]).getContents();
                if (StringUtil.isNotEmpty(serviceParameter)) {
                    return serviceParameter;
                }
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String signature, Project project) {

        //PhpIndex phpIndex = PhpIndex.getInstance(project);
        //FileBasedIndex fileBasedIndex = FileBasedIndex.getInstance();
        //List<ServiceSerializable> models = fileBasedIndex.getValues(ModelsDefinitionStubIndex.KEY, signature, GlobalSearchScope.projectScope(project));
        //
        //if (models.size() > 0) {
        //    Collection<PhpClass> phpClasses = new HashSet<>();
        //    for (ServiceSerializable model : models) {
        //        phpClasses.addAll(phpIndex.getClassesByFQN(model.getClassName()));
        //    }
        //    return phpClasses;
        //}

        return null;
    }
}
