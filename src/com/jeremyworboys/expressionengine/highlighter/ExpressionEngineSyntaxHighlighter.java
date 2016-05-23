package com.jeremyworboys.expressionengine.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.parser.ExpressionEngineLexerAdapter;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ExpressionEngineSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey COMMENT = createTextAttributesKey("EE_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey NUMBER = createTextAttributesKey("EE_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING = createTextAttributesKey("EE_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey BOOLEAN = createTextAttributesKey("EE_BOOLEAN", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey OPERATOR = createTextAttributesKey("EE_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey TAG = createTextAttributesKey("EE_TAG", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey TAG_PARAM = createTextAttributesKey("EE_TAG_PARAM", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey PARENTHESES = createTextAttributesKey("EE_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] BOOLEAN_KEYS = new TextAttributesKey[]{BOOLEAN};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATOR};
    private static final TextAttributesKey[] TAG_KEYS = new TextAttributesKey[]{TAG};
    private static final TextAttributesKey[] TAG_PARAM_KEYS = new TextAttributesKey[]{TAG_PARAM};
    private static final TextAttributesKey[] PARENTHESES_KEYS = new TextAttributesKey[]{PARENTHESES};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ExpressionEngineLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (isPartOfComment(tokenType)) {
            return COMMENT_KEYS;
        } else if (isPartOfNumber(tokenType)) {
            return NUMBER_KEYS;
        } else if (isPartOfString(tokenType)) {
            return STRING_KEYS;
        } else if (isBoolean(tokenType)) {
            return BOOLEAN_KEYS;
        } else if (isPartOfTag(tokenType)) {
            return TAG_KEYS;
        } else if (isPartOfTagParam(tokenType)) {
            return TAG_PARAM_KEYS;
        } else if (isOperator(tokenType)) {
            return OPERATOR_KEYS;
        } else if (isParentheses(tokenType)) {
            return PARENTHESES_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }

    private static boolean isPartOfComment(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_COMMENT);
    }

    private static boolean isPartOfNumber(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_NUMBER_LITERAL);
    }

    private static boolean isPartOfString(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_PATH_LITERAL)
            || tokenType.equals(ExpressionEngineTypes.T_STRING_CONTENT)
            || tokenType.equals(ExpressionEngineTypes.T_STRING_START)
            || tokenType.equals(ExpressionEngineTypes.T_STRING_END);
    }

    private static boolean isBoolean(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_TRUE)
            || tokenType.equals(ExpressionEngineTypes.T_FALSE);
    }

    private static boolean isPartOfTag(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_LD)
            || tokenType.equals(ExpressionEngineTypes.T_RD)
            || tokenType.equals(ExpressionEngineTypes.T_SLASH)
            || tokenType.equals(ExpressionEngineTypes.T_PATH)
            || tokenType.equals(ExpressionEngineTypes.T_EMBED)
            || tokenType.equals(ExpressionEngineTypes.T_LAYOUT)
            || tokenType.equals(ExpressionEngineTypes.T_REDIRECT)
            || tokenType.equals(ExpressionEngineTypes.T_VARIABLE_NAME)
            || tokenType.equals(ExpressionEngineTypes.T_MODULE_NAME)
            || tokenType.equals(ExpressionEngineTypes.T_IF)
            || tokenType.equals(ExpressionEngineTypes.T_ELSEIF)
            || tokenType.equals(ExpressionEngineTypes.T_ELSE);
    }

    private static boolean isPartOfTagParam(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_PARAM_NAME)
            || tokenType.equals(ExpressionEngineTypes.T_EQUAL);
    }

    private boolean isOperator(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_OP_ADD)
            || tokenType.equals(ExpressionEngineTypes.T_OP_AND)
            || tokenType.equals(ExpressionEngineTypes.T_OP_CONCAT)
            || tokenType.equals(ExpressionEngineTypes.T_OP_CONTAINS)
            || tokenType.equals(ExpressionEngineTypes.T_OP_DIV)
            || tokenType.equals(ExpressionEngineTypes.T_OP_ENDS)
            || tokenType.equals(ExpressionEngineTypes.T_OP_EQ)
            || tokenType.equals(ExpressionEngineTypes.T_OP_GT)
            || tokenType.equals(ExpressionEngineTypes.T_OP_GTE)
            || tokenType.equals(ExpressionEngineTypes.T_OP_LT)
            || tokenType.equals(ExpressionEngineTypes.T_OP_LTE)
            || tokenType.equals(ExpressionEngineTypes.T_OP_MATCH)
            || tokenType.equals(ExpressionEngineTypes.T_OP_MOD)
            || tokenType.equals(ExpressionEngineTypes.T_OP_MUL)
            || tokenType.equals(ExpressionEngineTypes.T_OP_NEQ)
            || tokenType.equals(ExpressionEngineTypes.T_OP_NOT)
            || tokenType.equals(ExpressionEngineTypes.T_OP_OR)
            || tokenType.equals(ExpressionEngineTypes.T_OP_POW)
            || tokenType.equals(ExpressionEngineTypes.T_OP_STARTS)
            || tokenType.equals(ExpressionEngineTypes.T_OP_SUB)
            || tokenType.equals(ExpressionEngineTypes.T_OP_XOR);
    }

    private static boolean isParentheses(IElementType tokenType) {
        return tokenType.equals(ExpressionEngineTypes.T_LP)
            || tokenType.equals(ExpressionEngineTypes.T_RP);
    }
}
