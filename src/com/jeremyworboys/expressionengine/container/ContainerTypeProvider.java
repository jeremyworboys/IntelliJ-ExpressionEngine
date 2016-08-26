package com.jeremyworboys.expressionengine.container;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.containers.HashSet;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jeremyworboys.expressionengine.stubs.indexes.ServicesDefinitionStubIndex;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ContainerTypeProvider implements PhpTypeProvider2 {
    @Override
    public char getKey() {
        return '\u0208';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement) {
        // Don't lookup if in low-power mode or isn't a configured ee3 project
        if (DumbService.getInstance(psiElement.getProject()).isDumb()
            || !ExpressionEngineProjectComponent.isEnabledVersion3(psiElement)) {
            return null;
        }

        if (psiElement instanceof FunctionReference){
            FunctionReference functionReference = (FunctionReference) psiElement;
            if ("ee".equals(functionReference.getName())) {
                PsiElement[] functionParameters = functionReference.getParameters();
                if (functionParameters.length > 0 && functionParameters[0] instanceof StringLiteralExpression) {
                    String serviceParameter = ((StringLiteralExpression) functionParameters[0]).getContents();
                    if (StringUtils.isNotBlank(serviceParameter)) {
                        return serviceParameter;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String signature, Project project) {

        PhpIndex phpIndex = PhpIndex.getInstance(project);
        FileBasedIndex fileBasedIndex = FileBasedIndex.getInstance();
        List<ServiceSerializable> services = fileBasedIndex.getValues(ServicesDefinitionStubIndex.KEY, signature, GlobalSearchScope.projectScope(project));

        if (services.size() > 0) {
            Collection<PhpClass> phpClasses = new HashSet<>();
            for (ServiceSerializable service : services) {
                phpClasses.addAll(phpIndex.getClassesByFQN(service.getClassName()));
            }
            return phpClasses;
        }

        return null;
    }
}
