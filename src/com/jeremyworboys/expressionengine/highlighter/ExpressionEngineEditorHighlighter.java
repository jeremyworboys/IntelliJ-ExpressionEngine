package com.jeremyworboys.expressionengine.highlighter;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpressionEngineEditorHighlighter extends LayeredLexerEditorHighlighter {
    public ExpressionEngineEditorHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme colors) {
        super(new ExpressionEngineSyntaxHighlighter(), colors);

        FileType fileType = null;
        if (project == null || virtualFile == null) {
            fileType = StdFileTypes.PLAIN_TEXT;
        } else {
            Language language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile);
            // Check for registered mapping for this file type
            if (language != null) {
                fileType = language.getAssociatedFileType();
            }
            // Check if file has css extension and CSS language exists
            if (fileType == null) {
                String fileExtension = virtualFile.getExtension();
                if (fileExtension != null && fileExtension.equalsIgnoreCase("css")) {
                    language = Language.findLanguageByID("CSS");
                    if (language != null) {
                        fileType = language.getAssociatedFileType();
                    }
                }
            }
            // Default to HTML file type
            if (fileType == null) {
                fileType = StdFileTypes.HTML;
            }
        }

        SyntaxHighlighter outerHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(fileType, project, virtualFile);

        registerLayer(ExpressionEngineTypes.T_HTML, new LayerDescriptor(outerHighlighter, ""));
    }
}
