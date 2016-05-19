package com.jeremyworboys.expressionengine;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ExpressionEngineSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("EE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

    private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ExpressionEngineLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(ExpressionEngineTypes.IDENTIFIER)) {
            return IDENTIFIER_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
