package com.jeremyworboys.expressionengine.template.highlighter;

import com.intellij.lang.StdLanguages;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpressionEngineEditorHighlighter extends LayeredLexerEditorHighlighter {
    public ExpressionEngineEditorHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme colors) {
        super(new ExpressionEngineSyntaxHighlighter(), colors);

        SyntaxHighlighter htmlHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(StdLanguages.HTML, project, virtualFile);
        LayerDescriptor htmlLayer = new LayerDescriptor(htmlHighlighter, "");
        registerLayer(ExpressionEngineTypes.T_CONTENT, htmlLayer);
    }
}
