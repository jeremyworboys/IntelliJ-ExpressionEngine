package com.jeremyworboys.expressionengine;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import com.intellij.psi.TokenType;
import java.util.Stack;

%%

// Lexer class generation configuration
%class ExpressionEngineLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}
//%debug

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

// Generic macros
CRLF=\n|\r|\r\n
WS=[\ \t\f]

IDENTIFIER=[a-zA-Z][a-zA-Z0-9:_-]*[a-zA-Z]+

// ExpressionEngine tag delimiters
LD=\{
RD=\}

%state IN_EE_TAG

%%

<YYINITIAL> {
  {LD}                                 { pushState(IN_EE_TAG); return ExpressionEngineTypes.LD; }

  {CRLF}                               { return ExpressionEngineTypes.CRLF; }
  {WS}+                                { return TokenType.WHITE_SPACE; }
}

<IN_EE_TAG> {
  {RD}                                 { popState(); return ExpressionEngineTypes.RD; }

  {IDENTIFIER}                         { return ExpressionEngineTypes.IDENTIFIER; }
}

.                                      { return ExpressionEngineTypes.HTML; }
