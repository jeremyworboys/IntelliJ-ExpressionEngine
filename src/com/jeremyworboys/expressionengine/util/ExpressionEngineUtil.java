package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.TokenType;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.jeremyworboys.expressionengine.ExpressionEngineFileType;
import com.jeremyworboys.expressionengine.ExpressionEngineLanguage;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineFile;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpressionEngineUtil {
    @NotNull
    public static ElementPattern<PsiElement> getTemplateFileReferencePattern() {
        return getTemplateFileReferencePattern("layout", "embed");
    }

    @NotNull
    public static ElementPattern<PsiElement> getTemplateFileReferencePattern(String... tagNames) {
        // Matches: {embed="<xxx>"}
        //noinspection unchecked
        ElementPattern<PsiElement> stringPattern =
            PlatformPatterns
                .psiElement(ExpressionEngineTypes.T_STRING)
                .withParent(PlatformPatterns.psiElement(ExpressionEngineTypes.TAG_PARAM_VALUE))
                .afterLeafSkipping(
                    PlatformPatterns.or(
                        PlatformPatterns.psiElement(TokenType.WHITE_SPACE),
                        PlatformPatterns.psiElement(ExpressionEngineTypes.T_LD),
                        PlatformPatterns.psiElement(ExpressionEngineTypes.T_EQUALS),
                        PlatformPatterns.psiElement(ExpressionEngineTypes.T_STRING_START)
                    ),
                    PlatformPatterns.psiElement(ExpressionEngineTypes.T_PARAM_VAR).withText(PlatformPatterns.string().oneOf(tagNames))
                )
                .withLanguage(ExpressionEngineLanguage.INSTANCE);

        // Matches: {embed=<xxx>}
        //noinspection unchecked
        ElementPattern<PsiElement> pathPattern =
            PlatformPatterns
                .psiElement(ExpressionEngineTypes.T_PATH)
                .withParent(PlatformPatterns.psiElement(ExpressionEngineTypes.TAG_PARAM_VALUE))
                .afterLeafSkipping(
                    PlatformPatterns.or(
                        PlatformPatterns.psiElement(TokenType.WHITE_SPACE),
                        PlatformPatterns.psiElement(ExpressionEngineTypes.T_LD),
                        PlatformPatterns.psiElement(ExpressionEngineTypes.T_EQUALS)
                    ),
                    PlatformPatterns.psiElement(ExpressionEngineTypes.T_PARAM_VAR).withText(PlatformPatterns.string().oneOf(tagNames))
                )
                .withLanguage(ExpressionEngineLanguage.INSTANCE);

        //noinspection unchecked
        return PlatformPatterns.or(stringPattern, pathPattern);
    }

    @NotNull
    public static List<ExpressionEngineFile> getTemplateFiles(@NotNull Project project, String templateName) {
        List<ExpressionEngineFile> result = new ArrayList<>();

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance()
            .getContainingFiles(FileTypeIndex.NAME, ExpressionEngineFileType.INSTANCE, scope);

        for (VirtualFile virtualFile : virtualFiles) {
            if (isMatchingTemplateName(virtualFile, templateName)) {
                ExpressionEngineFile expressionEngineFile = (ExpressionEngineFile) PsiManager.getInstance(project).findFile(virtualFile);
                if (expressionEngineFile != null) {
                    result.add(expressionEngineFile);
                }
            }
        }

        return result;
    }

    private static boolean isMatchingTemplateName(VirtualFile virtualFile, String templateName) {
        String templatePath = toTemplatePath(templateName);
        if (templatePath != null) {
            return virtualFile.getPath().endsWith(templatePath);
        }

        return false;
    }

    @Nullable
    private static String toTemplatePath(String templateName) {
        String[] parts = templateName.split("\\/");

        if (parts.length == 2) {
            return parts[0] + ".group/" + parts[1] + ".html";
        }

        return null;
    }
}
