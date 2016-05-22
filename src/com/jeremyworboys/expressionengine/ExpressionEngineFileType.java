package com.jeremyworboys.expressionengine;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.jeremyworboys.expressionengine.highlighter.ExpressionEngineEditorHighlighter;
import com.jeremyworboys.expressionengine.icons.ExpressionEngineIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExpressionEngineFileType extends LanguageFileType {
    public static final ExpressionEngineFileType INSTANCE = new ExpressionEngineFileType();

    private ExpressionEngineFileType() {
        super(ExpressionEngineLanguage.INSTANCE);

        FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider() {
            public EditorHighlighter getEditorHighlighter(@Nullable Project project, @NotNull FileType fileType, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme colors) {
                return new ExpressionEngineEditorHighlighter(project, virtualFile, colors);
            }
        });
    }

    @NotNull
    @Override
    public String getName() {
        return "ExpressionEngine";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "ExpressionEngine template file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "html";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return ExpressionEngineIcons.FILE;
    }

    @NonNls
    public String[] getExtensions() {
        return new String[]{getDefaultExtension(), "css"};
    }
}
