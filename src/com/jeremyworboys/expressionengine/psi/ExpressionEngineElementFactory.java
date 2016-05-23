package com.jeremyworboys.expressionengine.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.jeremyworboys.expressionengine.ExpressionEngineFileType;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineElementFactory {
    @NotNull
    public static ExpressionEnginePathLiteral createPathLiteral(@NotNull Project project, @NotNull String content) {
        final ExpressionEngineFile file = createDummyFile(project, "{path=" + content + "}");
        final ExpressionEngineTag tag = (ExpressionEngineTag) file.getFirstChild();
        //noinspection ConstantConditions
        return (ExpressionEnginePathLiteral) tag.getTagParamValue().getFirstChild();
    }

    @NotNull
    public static ExpressionEngineStringLiteral createStringLiteral(Project project, String content) {
        final ExpressionEngineFile file = createDummyFile(project, "{path=\"" + content + "\"}");
        final ExpressionEngineTag tag = (ExpressionEngineTag) file.getFirstChild();
        //noinspection ConstantConditions
        return (ExpressionEngineStringLiteral) tag.getTagParamValue().getFirstChild();
    }

    @NotNull
    public static ExpressionEngineFile createDummyFile(@NotNull Project project, @NotNull String content) {
        String fileName = "dummy." + ExpressionEngineFileType.INSTANCE.getDefaultExtension();
        return (ExpressionEngineFile) PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, ExpressionEngineFileType.INSTANCE, content);
    }
}
