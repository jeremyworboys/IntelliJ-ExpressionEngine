package com.jeremyworboys.expressionengine.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ExpressionEngineHighlighter {
    public static final TextAttributesKey COMMENT = createTextAttributesKey("EE_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey NUMBER = createTextAttributesKey("EE_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING = createTextAttributesKey("EE_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey BOOLEAN = createTextAttributesKey("EE_BOOLEAN", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey OPERATOR = createTextAttributesKey("EE_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey TAG = createTextAttributesKey("EE_TAG", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey TAG_PARAM = createTextAttributesKey("EE_TAG_PARAM", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey PARENTHESES = createTextAttributesKey("EE_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);

    public ExpressionEngineHighlighter() {
    }
}
