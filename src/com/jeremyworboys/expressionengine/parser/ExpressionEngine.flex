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
WS=[\ \t\f\s]
CRLF=(\n|\r|\r\n)
WHITE_SPACE={WS}+ | {CRLF}

LD="{"
RD="}"
SLASH="/"
EQUAL="="
COLON=":"

PATH=[a-zA-Z0-9\-_\.]+("/"[a-zA-Z0-9\-_\.]+)*
NUMBER=([0-9]*\.[0-9]+|[0-9]+\.[0-9]*|[0-9]+)
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_-]*

SINGLE_QUOTE="\'"
DOUBLE_QUOTE="\""

COMMENT_OPEN="{!--"
COMMENT_END="--}"
COMMENT={COMMENT_OPEN} ~ {COMMENT_END}

MODULE_NAME="exp:" {IDENTIFIER} (":" {IDENTIFIER})?
VARIABLE_NAME={IDENTIFIER} (":" {IDENTIFIER})*

// States
%state IN_EE_COMMENT
%state IN_EE_TAG
%state IN_EE_TAG_PARAMS
%state IN_EE_PRELOAD_REPLACE
%state IN_EE_EXPRESSION
%state IN_SINGLE_STRING
%state IN_DOUBLE_STRING

%%

{WHITE_SPACE}                          { return TokenType.WHITE_SPACE; }
{COMMENT}                              { yypushback(yylength() - 4); pushState(IN_EE_COMMENT); return T_COMMENT_START; }
{LD}{WHITE_SPACE}                      { return T_CONTENT; }
{LD}{RD}                               { return T_CONTENT; }
{LD}{DOUBLE_QUOTE}                     { return T_CONTENT; }
{LD}{SINGLE_QUOTE}                     { return T_CONTENT; }

<YYINITIAL> {
  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
  !([^]*"{"[^]*)                       { return determineContentToken(); }
}

<IN_EE_COMMENT> {
  {COMMENT_END}                      { popState(); return T_COMMENT_END; }
  ~ {COMMENT_END}                    { yypushback(3); return T_COMMENT; }
}

<IN_EE_TAG> {
  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
  {RD}                                 { popState(); return T_RD; }
  {SLASH}                              { return T_SLASH; }
  // Conditional
  "if"                                 { pushState(IN_EE_EXPRESSION); return T_IF; }
  "if:elseif"                          { pushState(IN_EE_EXPRESSION); return T_ELSEIF; }
  "if:else"                            { return T_ELSE; }
  // Special tags
  "path="                              { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_PATH; }
  "route="                             { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_ROUTE; }
  "embed="                             { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_EMBED; }
  "layout="                            { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_LAYOUT; }
  "redirect="                          { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_REDIRECT; }
  "switch="                            { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_SWITCH; }
  "encode="                            { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_ENCODE; }
  "stylesheet="                        { yypushback(1); pushState(IN_EE_TAG_PARAMS); return T_STYLESHEET; }
  "preload_replace" ~ {RD}             { yypushback(yylength() - 15); pushState(IN_EE_PRELOAD_REPLACE); return T_PRELOAD_REPLACE; }
  // Module tags
  {MODULE_NAME}                        { pushState(IN_EE_TAG_PARAMS); return T_MODULE_NAME; }
  // Variable tags
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
  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
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
