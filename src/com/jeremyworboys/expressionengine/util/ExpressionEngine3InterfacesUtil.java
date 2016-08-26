package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpressionEngine3InterfacesUtil {

    public static boolean isContainerGetCall(Method psiElement) {
        return isCallTo(psiElement, new Method[]{
            getInterfaceMethod(psiElement.getProject(), "\\EllisLab\\ExpressionEngine\\Service\\Dependency\\ServiceProvider", "make"),
            getClassMethod(psiElement.getProject(), "\\EllisLab\\ExpressionEngine\\Service\\Dependency\\InjectionContainer", "make"),
        });
    }

    public static boolean isModelFactoryCall(Method psiElement) {
        return isCallTo(psiElement, new Method[]{
            getClassMethod(psiElement.getProject(), "\\EllisLab\\ExpressionEngine\\Service\\Model\\Facade", "make"),
        });
    }

    private static boolean isCallTo(Method method, Method[] expectedMethods) {
        PhpClass methodClass = method.getContainingClass();
        if (methodClass == null) {
            return false;
        }

        for (Method expectedMethod : expectedMethods) {
            if (expectedMethod == null) {
                continue;
            }

            PhpClass expectedClass = expectedMethod.getContainingClass();
            if (expectedClass != null
                && expectedMethod.getName().equals(method.getName())
                && isInstanceOf(methodClass, expectedClass)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isInstanceOf(@NotNull PhpClass subjectClass, @NotNull PhpClass expectedClass) {
        PhpType subjectType = new PhpType().add(subjectClass);
        PhpType expectedType = new PhpType().add(expectedClass);
        PhpIndex phpIndex = PhpIndex.getInstance(subjectClass.getProject());

        return expectedType.isConvertibleFrom(subjectType, phpIndex);
    }

    @Nullable
    private static Method getInterfaceMethod(@NotNull Project project, @NotNull String phpInterfaceName, @NotNull String methodName) {
        for (PhpClass phpInterface : PhpIndex.getInstance(project).getInterfacesByFQN(phpInterfaceName)) {
            Method method = phpInterface.findMethodByName(methodName);
            if (method != null) {
                return method;
            }
        }

        return null;
    }

    @Nullable
    private static Method getClassMethod(@NotNull Project project, @NotNull String phpClassName, @NotNull String methodName) {
        for (PhpClass phpClass : PhpIndex.getInstance(project).getClassesByFQN(phpClassName)) {
            Method method = phpClass.findMethodByName(methodName);
            if (method != null) {
                return method;
            }
        }

        return null;
    }
}
