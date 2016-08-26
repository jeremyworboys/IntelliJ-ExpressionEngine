package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhpTypeProviderUtil {

    public static boolean shouldProvideTypeCompletion(PsiElement psiElement) {
        return !DumbService.getInstance(psiElement.getProject()).isDumb();
    }

    public static boolean shouldProvideTypeCompletion3(PsiElement psiElement) {
        return shouldProvideTypeCompletion(psiElement)
            && ExpressionEngineProjectComponent.isEnabledVersion3(psiElement);
    }

    @Nullable
    public static String getReferenceSignature(MethodReference methodReference, char trimKey) {
        return getReferenceSignature(methodReference, trimKey, 1);
    }

    @Nullable
    public static String getReferenceSignature(MethodReference methodReference, char trimKey, int equalParameterCount) {

        String[] methodSignatures = methodReference.getSignature().split("\\|");
        if (methodSignatures.length == 0) {
            return null;
        }

        PsiElement[] parameters = methodReference.getParameters();
        if (parameters.length != equalParameterCount) {
            return null;
        }

        PsiElement parameter = parameters[0];

        // we already have a string value
        if (parameter instanceof StringLiteralExpression) {
            String param = ((StringLiteralExpression) parameter).getContents();
            if (StringUtil.isNotEmpty(param)) {
                return buildReferenceSignature(methodSignatures, trimKey, param);
            }
        }

        // whitelist here; we can also provide some more but think of performance
        // Service::NAME, $this->name and Entity::CLASS;
        if (parameter instanceof PhpReference && (parameter instanceof ClassConstantReference || parameter instanceof FieldReference)) {
            String signature = ((PhpReference) parameter).getSignature();
            if (StringUtil.isNotEmpty(signature)) {
                return buildReferenceSignature(methodSignatures, trimKey, signature);
            }
        }

        return null;
    }

    private static String buildReferenceSignature(String[] methodSignatures, char trimKey, String param) {
        List<String> signatures = new ArrayList<>();

        for (String methodSignature : methodSignatures) {
            signatures.add(methodSignature + trimKey + param);
        }

        return String.join("|", signatures);
    }

    /**
     * we can also pipe php references signatures and resolve them here
     * overwrite parameter to get string value
     */
    @Nullable
    public static String getResolvedParameter(@NotNull PhpIndex phpIndex, @NotNull String parameter) {

        // PHP 5.5 class constant: "Class\Foo::class"
        if (parameter.startsWith("#K#C")) {
            // PhpStorm9: #K#C\Class\Foo.class
            if (parameter.endsWith(".class")) {
                return parameter.substring(4, parameter.length() - 6);
            }

            // PhpStorm8: #K#C\Class\Foo.
            // workaround since signature has empty type
            if (parameter.endsWith(".")) {
                return parameter.substring(4, parameter.length() - 1);
            }
        }

        // #K#C\Class\Foo.property
        // #K#C\Class\Foo.CONST
        if (parameter.startsWith("#")) {

            // get psi element from signature
            Collection<? extends PhpNamedElement> signatureTypes = phpIndex.getBySignature(parameter, null, 0);
            if (signatureTypes.size() == 0) {
                return null;
            }

            // get string value
            parameter = PhpElementsUtil.getStringValue(signatureTypes.iterator().next());
        }

        return parameter;
    }

    /**
     * We can have multiple types inside a TypeProvider; split them on "|" so that we dont get empty types
     *
     * #M#x#M#C\FooBar.get?doctrine.odm.mongodb.document_manager.getRepository|
     * #M#x#M#C\FooBar.get?doctrine.odm.mongodb.document_manager.getRepository
     */
    @NotNull
    public static Collection<? extends PhpNamedElement> getTypeSignature(@NotNull PhpIndex phpIndex, @NotNull String signatures) {

        if (!signatures.contains("|")) {
            return phpIndex.getBySignature(signatures, null, 0);
        }

        Collection<PhpNamedElement> elements = new ArrayList<>();
        for (String signature : signatures.split("\\|")) {
            elements.addAll(phpIndex.getBySignature(signature, null, 0));
        }

        return elements;
    }
}
