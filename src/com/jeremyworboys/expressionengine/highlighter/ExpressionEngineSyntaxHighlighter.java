package com.jeremyworboys.expressionengine.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.lexer.ExpressionEngineLexerAdapter;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTokenSets;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ExpressionEngineSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ExpressionEngineLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        TextAttributesKey key = ATTRIBUTES.get(tokenType);
        return (key != null) ? pack(key) : new TextAttributesKey[0];
    }

    static {
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.COMMENTS, ExpressionEngineHighlighter.COMMENT);
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.NUMBERS, ExpressionEngineHighlighter.NUMBER);
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.STRINGS, ExpressionEngineHighlighter.STRING);
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.BOOLEANS, ExpressionEngineHighlighter.BOOLEAN);
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.TAGS, ExpressionEngineHighlighter.TAG);
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.TAG_PARAMS, ExpressionEngineHighlighter.TAG_PARAM);
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.OPERATORS, ExpressionEngineHighlighter.OPERATOR);
        fillMap(ATTRIBUTES, ExpressionEngineTokenSets.PARENTHESES, ExpressionEngineHighlighter.PARENTHESES);
    }
}
