package com.jeremyworboys.expressionengine.psi;

import com.intellij.psi.tree.TokenSet;

public class ExpressionEngineTokenSets {
    public static final TokenSet COMMENTS;
    public static final TokenSet NUMBERS;
    public static final TokenSet STRINGS;
    public static final TokenSet BOOLEANS;
    public static final TokenSet TAGS;
    public static final TokenSet TAG_PARAMS;
    public static final TokenSet OPERATORS;
    public static final TokenSet PARENTHESES;

    private ExpressionEngineTokenSets() {
    }

    static {
        COMMENTS = TokenSet.create(
            ExpressionEngineTypes.T_COMMENT,
            ExpressionEngineTypes.T_COMMENT_START,
            ExpressionEngineTypes.T_COMMENT_END);
        NUMBERS = TokenSet.create(
            ExpressionEngineTypes.T_NUMBER_LITERAL);
        STRINGS = TokenSet.create(
            ExpressionEngineTypes.T_PATH_LITERAL,
            ExpressionEngineTypes.T_STRING_CONTENT,
            ExpressionEngineTypes.T_STRING_START,
            ExpressionEngineTypes.T_STRING_END);
        BOOLEANS = TokenSet.create(
            ExpressionEngineTypes.T_TRUE,
            ExpressionEngineTypes.T_FALSE);
        TAGS = TokenSet.create(
            ExpressionEngineTypes.T_LD,
            ExpressionEngineTypes.T_RD,
            ExpressionEngineTypes.T_COLON,
            ExpressionEngineTypes.T_SLASH,
            ExpressionEngineTypes.T_PATH,
            ExpressionEngineTypes.T_ROUTE,
            ExpressionEngineTypes.T_EMBED,
            ExpressionEngineTypes.T_LAYOUT,
            ExpressionEngineTypes.T_REDIRECT,
            ExpressionEngineTypes.T_SWITCH,
            ExpressionEngineTypes.T_ENCODE,
            ExpressionEngineTypes.T_STYLESHEET,
            ExpressionEngineTypes.T_PRELOAD_REPLACE,
            ExpressionEngineTypes.T_VARIABLE_NAME,
            ExpressionEngineTypes.T_MODULE_NAME,
            ExpressionEngineTypes.T_IF,
            ExpressionEngineTypes.T_ELSEIF,
            ExpressionEngineTypes.T_ELSE);
        TAG_PARAMS = TokenSet.create(
            ExpressionEngineTypes.T_PARAM_NAME,
            ExpressionEngineTypes.T_EQUAL);
        OPERATORS = TokenSet.create(
            ExpressionEngineTypes.T_OP_ADD,
            ExpressionEngineTypes.T_OP_AND,
            ExpressionEngineTypes.T_OP_CONCAT,
            ExpressionEngineTypes.T_OP_CONTAINS,
            ExpressionEngineTypes.T_OP_DIV,
            ExpressionEngineTypes.T_OP_ENDS,
            ExpressionEngineTypes.T_OP_EQ,
            ExpressionEngineTypes.T_OP_GT,
            ExpressionEngineTypes.T_OP_GTE,
            ExpressionEngineTypes.T_OP_LT,
            ExpressionEngineTypes.T_OP_LTE,
            ExpressionEngineTypes.T_OP_MATCH,
            ExpressionEngineTypes.T_OP_MOD,
            ExpressionEngineTypes.T_OP_MUL,
            ExpressionEngineTypes.T_OP_NEQ,
            ExpressionEngineTypes.T_OP_NOT,
            ExpressionEngineTypes.T_OP_OR,
            ExpressionEngineTypes.T_OP_POW,
            ExpressionEngineTypes.T_OP_STARTS,
            ExpressionEngineTypes.T_OP_SUB,
            ExpressionEngineTypes.T_OP_XOR);
        PARENTHESES = TokenSet.create(
            ExpressionEngineTypes.T_LP,
            ExpressionEngineTypes.T_RP);
    }
}
