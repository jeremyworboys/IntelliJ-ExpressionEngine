package com.jeremyworboys.expressionengine.parser;

import com.intellij.lexer.FlexLexer;
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
%}

// Macros
WS=[\ \t\f]
CRLF=(\n|\r|\r\n)

LD="{"
RD="}"

COMMENT="{!--" ~"--}"

// States
%state IN_EE_TAG

%%

{WS}                                   { return T_WS; }
{CRLF}                                 { return T_CRLF; }
{COMMENT}                              { return T_COMMENT; }

<YYINITIAL> {
  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
  ~ {LD}                               { yypushback(1); return T_CONTENT; }
}

<IN_EE_TAG> {
  {RD}                                 { popState(); return T_RD; }
  ~ {RD}                               { yypushback(1); return T_TAG_CONTENT; }
}

[^]                                    { return TokenType.BAD_CHARACTER; }
