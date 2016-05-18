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
CRLF= \n|\r|\r\n
WHITE_SPACE= {CRLF} | [\ \t\f]

// ExpressionEngine delimiters
LD=\{
RD=\}

%state EE_TAG

%%

{LD}                        { pushState(EE_TAG); return ExpressionEngineTypes.LD; }

<EE_TAG> {
  {RD}                      { popState(); return ExpressionEngineTypes.RD; }
}

.                           { return ExpressionEngineTypes.HTML; }
