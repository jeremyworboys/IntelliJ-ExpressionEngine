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

// Generics
CRLF=\n|\r|\r\n
WS=[\ \t\f]

EQUAL==
SINGLE_QUOTE="\'"
DOUBLE_QUOTE="\""

// Numbers
NUMBER={HEX_NUMBER}|{DEC_NUMBER}
HEX_NUMBER=0(x|X)[0-9a-fA-F]*
DEC_NUMBER=([0-9]+\.?[0-9]*)|(\.[0-9]+){EXP_NUMBER}?
EXP_NUMBER=(e|E)(\+|-)?[0-9]+

// Identifiers
IDENTIFIER=[a-zA-Z][a-zA-Z0-9:_-]*[a-zA-Z]+

// ExpressionEngine tag delimiters
LD=\{
RD=\}

// ExpressionEngine tag types
TAG_BUILTIN=(\/?exp:(channel:(entries|calendar|month_links|next_entry|prev_entry|categories|category_archive|category_heading|info)|comment:(ajax_edit_url|edit_comment_script|entries|notification_links|subscriber_list|form|preview)|cookie_consent:message|email:(contact_form|tell_a_friend)|emoticon|file:entries|ip_to_nation:world_flags|jquery:(script_tag|script_src|output_javascript)|mailinglist:form|member:(login_form|custom_profile_data|ignore_list)|moblog:check|pages:load_site_pages|query|referrer|rss:feed|rte:script_url|forum:forum_helper|forum:topic_titles|forum|xml_encode|safecracker|search:(advanced_form|simple_form|search_results|total_results|keywords)|magpie|updated_sites:pings|stats|simple_commerce:purchase|wiki)|wiki:(categories|category_subcategories|category_articles|files|recent_changes|search_results|title_list|associated_articles|associated_pages|custom_namespaces_list))\b
TAG_ADDON=(\/?exp:[a-zA-Z0-9_:]+\b)
TAG_CONSTANT=(DATE_ATOM|DATE_COOKIE|DATE_ISO8601|DATE_RFC822|DATE_RFC850|DATE_RFC1036|DATE_RFC1123|DATE_RFC2822|DATE_RSS|DATE_W3C|XID_HASH)
TAG_DEPRECATED=(\/?exp:weblog:[^\s\}]*|exp:channel:entry_form|exp:trackback:[^\s\}]*|exp:gallery:[^\s\}]*|display_custom_fields|saef_javascript)\b

%state IN_EE_TAG
%state IN_SINGLE_STRING
%state IN_DOUBLE_STRING

%%

<YYINITIAL> {
  {LD}                                 { pushState(IN_EE_TAG); return ExpressionEngineTypes.LD; }

  {CRLF}                               { return ExpressionEngineTypes.CRLF; }
  {WS}+                                { return TokenType.WHITE_SPACE; }
}

<IN_EE_TAG> {
  {RD}                                 { popState(); return ExpressionEngineTypes.RD; }

  {TAG_BUILTIN}                        { return ExpressionEngineTypes.TAG_BUILTIN; }
  {TAG_ADDON}                          { return ExpressionEngineTypes.TAG_ADDON; }
  {TAG_CONSTANT}                       { return ExpressionEngineTypes.TAG_CONSTANT; }
  {TAG_DEPRECATED}                     { return ExpressionEngineTypes.TAG_DEPRECATED; }
  {IDENTIFIER}                         { return ExpressionEngineTypes.IDENTIFIER; }

  {EQUAL}                              { return ExpressionEngineTypes.EQUAL; }
  {NUMBER}                             { return ExpressionEngineTypes.NUMBER; }
  {SINGLE_QUOTE}                       { pushState(IN_SINGLE_STRING); return ExpressionEngineTypes.STRING_START; }
  {DOUBLE_QUOTE}                       { pushState(IN_DOUBLE_STRING); return ExpressionEngineTypes.STRING_START; }

  {CRLF}                               { return ExpressionEngineTypes.CRLF; }
  {WS}+                                { return TokenType.WHITE_SPACE; }
}

<IN_SINGLE_STRING> {
  ((\\.)|[^'{}])+                      { return ExpressionEngineTypes.STRING; }
  {SINGLE_QUOTE}                       { popState(); return ExpressionEngineTypes.STRING_END; }
}

<IN_DOUBLE_STRING> {
  ((\\.)|[^\"{}])+                     { return ExpressionEngineTypes.STRING; }
  {DOUBLE_QUOTE}                       { popState(); return ExpressionEngineTypes.STRING_END; }
}

.                                      { return ExpressionEngineTypes.HTML; }
