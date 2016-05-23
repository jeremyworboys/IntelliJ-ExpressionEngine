package com.jeremyworboys.expressionengine.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;

import java.util.Stack;

import static com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes.*;

%%

// Lexer class generation configuration
%class ExpressionEngineLexer
%implements FlexLexer
%unicode
%ignorecase
%function advance
%type IElementType
%eof{  return;
%eof}

// Custom class methods
%{
  private Stack<Integer> lexStateStack = new Stack<java.lang.Integer>();

  private void pushState(int state) {
    lexStateStack.push(yystate());
    yybegin(state);
  }

  private void popState() {
    if (lexStateStack.empty()) {
      yybegin(YYINITIAL);
    } else {
      yybegin(lexStateStack.pop());
    }
  }

  private IElementType determineContentToken() {
    if (yylength() == 0) {
      return null;
    }
    String captured = yytext().toString();
    if (StringUtil.trim(captured).length() == 0) {
      return TokenType.WHITE_SPACE;
    }
    // Ignore leading whitespace
    if (StringUtil.trimLeading(captured).length() != captured.length()) {
      yypushback(StringUtil.trimLeading(captured).length());
      return TokenType.WHITE_SPACE;
    }
    // Ignore trailing whitespace
    if (StringUtil.trimTrailing(captured).length() != captured.length()) {
      yypushback(captured.length() - StringUtil.trimTrailing(captured).length());
    }
    return T_CONTENT;
  }
%}

// Macros
WS=[\ \t\f]
CRLF=(\n|\r|\r\n)
WHITE_SPACE={WS}+ | {CRLF}

LD="{"
RD="}"
SLASH="/"
EQUAL="="
COLON=":"

PATH=[a-zA-Z0-9\-_\.]+("/"[a-zA-Z0-9\-_\.]+)*
NUMBER=([0-9]*\.[0-9]+|[0-9]+\.[0-9]*|[0-9]+)

SINGLE_QUOTE="\'"
DOUBLE_QUOTE="\""

COMMENT="{!--" ~"--}"
MODULE_NAME="exp:" [a-zA-Z][a-zA-Z0-9_]* (":" [a-zA-Z][a-zA-Z0-9_]*)?
VARIABLE_NAME=[a-zA-Z][a-zA-Z0-9_-]* (":" [a-zA-Z][a-zA-Z0-9_-]*)*

// States
%state IN_EE_TAG
%state IN_EE_TAG_PARAMS
%state IN_EE_PRELOAD_REPLACE
%state IN_EE_EXPRESSION
%state IN_SINGLE_STRING
%state IN_DOUBLE_STRING

%%

{WHITE_SPACE}                          { return TokenType.WHITE_SPACE; }
{COMMENT}                              { return T_COMMENT; }
{LD}{WHITE_SPACE}                      { return T_CONTENT; }

<YYINITIAL> {
  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
  !([^]*"{"[^]*)                       { return determineContentToken(); }
}

<IN_EE_TAG> {
  {RD}                                 { popState(); return T_RD; }
  {SLASH}                              { return T_SLASH; }
  // Conditional
  "if"                                 { pushState(IN_EE_EXPRESSION); return T_IF; }
  "if:elseif"                          { pushState(IN_EE_EXPRESSION); return T_ELSEIF; }
  "if:else"                            { return T_ELSE; }
  // Special tag
  "path="                              { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_PATH; }
  "embed"                              { pushState(IN_EE_TAG_PARAMS); return T_EMBED; }
  "layout"                             { pushState(IN_EE_TAG_PARAMS); return T_LAYOUT; }
  "redirect"                           { pushState(IN_EE_TAG_PARAMS); return T_REDIRECT; }
  "switch="                            { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_SWITCH; }
  "encode"                             { pushState(IN_EE_TAG_PARAMS); return T_ENCODE; }
  "stylesheet"                         { pushState(IN_EE_TAG_PARAMS); return T_STYLESHEET; }
  "preload_replace" ~ {RD}             { yypushback(yylength() - 15); pushState(IN_EE_PRELOAD_REPLACE); return T_PRELOAD_REPLACE; }
  // Module tag
  {MODULE_NAME}                        { pushState(IN_EE_TAG_PARAMS); return T_MODULE_NAME; }
  // Variable tag
  {VARIABLE_NAME}                      { pushState(IN_EE_TAG_PARAMS); return T_VARIABLE_NAME; }
}

<IN_EE_TAG_PARAMS> {
  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
  {RD}                                 { yypushback(1); popState(); }
  // Param
  {VARIABLE_NAME} "="                  { yypushback(1); return T_PARAM_NAME; }
  {EQUAL}                              { return T_EQUAL; }
  // Literals
  {PATH}                               { return T_PATH_LITERAL; }
  {NUMBER}                             { return T_NUMBER_LITERAL; }
  {SINGLE_QUOTE}                       { pushState(IN_SINGLE_STRING); return T_STRING_START; }
  {DOUBLE_QUOTE}                       { pushState(IN_DOUBLE_STRING); return T_STRING_START; }
}

<IN_EE_PRELOAD_REPLACE> {
  {RD}                                 { yypushback(1); popState(); }
  // Param
  {VARIABLE_NAME}                      { return T_PARAM_NAME; }
  {EQUAL}                              { return T_EQUAL; }
  {COLON}                              { return T_COLON; }
  // Literals
  {NUMBER}                             { return T_NUMBER_LITERAL; }
  {SINGLE_QUOTE}                       { pushState(IN_SINGLE_STRING); return T_STRING_START; }
  {DOUBLE_QUOTE}                       { pushState(IN_DOUBLE_STRING); return T_STRING_START; }
}

<IN_EE_EXPRESSION> {
  {RD}                                 { yypushback(1); popState(); }
  // Parens
  "("                                  { return T_LP; }
  ")"                                  { return T_RP; }
  // Operators
  "=="                                 { return T_OP_EQ; }
  "!="|"<>"                            { return T_OP_NEQ; }
  "<"                                  { return T_OP_LT; }
  "<="                                 { return T_OP_LTE; }
  ">"                                  { return T_OP_GT; }
  ">="                                 { return T_OP_GTE; }
  "^="                                 { return T_OP_STARTS; }
  "*="                                 { return T_OP_CONTAINS; }
  "$="                                 { return T_OP_ENDS; }
  "~"                                  { return T_OP_MATCH; }
  "!"                                  { return T_OP_NOT; }
  "&&"|"AND"                           { return T_OP_AND; }
  "XOR"                                { return T_OP_XOR; }
  "||"|"OR"                            { return T_OP_OR; }
  "+"                                  { return T_OP_ADD; }
  "-"                                  { return T_OP_SUB; }
  "*"                                  { return T_OP_MUL; }
  "/"                                  { return T_OP_DIV; }
  "**"|"^"                             { return T_OP_POW; }
  "%"                                  { return T_OP_MOD; }
  "."                                  { return T_OP_CONCAT; }
  // Literals
  "true"                               { return T_TRUE; }
  "false"                              { return T_FALSE; }
  {NUMBER}                             { return T_NUMBER_LITERAL; }
  {SINGLE_QUOTE}                       { pushState(IN_SINGLE_STRING); return T_STRING_START; }
  {DOUBLE_QUOTE}                       { pushState(IN_DOUBLE_STRING); return T_STRING_START; }
  {VARIABLE_NAME}                      { return T_VARIABLE_NAME; }
}

<IN_SINGLE_STRING> {
  // TODO: Match comments in strings
  // TODO: Match variables in strings
  {SINGLE_QUOTE}                       { popState(); return T_STRING_END; }
  ~{SINGLE_QUOTE}                      { yypushback(1); return T_STRING_CONTENT; }
}

<IN_DOUBLE_STRING> {
  // TODO: Match comments in strings
  // TODO: Match variables in strings
  {DOUBLE_QUOTE}                       { popState(); return T_STRING_END; }
  ~{DOUBLE_QUOTE}                      { yypushback(1); return T_STRING_CONTENT; }
}

[^]                                    { yybegin(YYINITIAL); return T_CONTENT; }
