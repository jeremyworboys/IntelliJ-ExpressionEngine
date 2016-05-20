package com.jeremyworboys.expressionengine;

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

// Generics
WS=[\ \t\f]
CRLF=\n|\r|\r\n

SINGLE_QUOTE="\'"
DOUBLE_QUOTE="\""

// Numbers
NUMBER=([0-9]*\.[0-9]+|[0-9]+\.[0-9]*|[0-9]+)

// Identifiers
VARIABLE=[a-zA-Z0-9\-_:]+
// TODO: Double check allowed chars for paths
PATH=[a-zA-Z0-9\-_]+([a-zA-Z0-9\-_/.]+)*
//VARIABLE=\w*([a-zA-Z]+([\w:-]+\w)?|(\w[\w:-]+)?[a-zA-Z]+)\w*
//IDENTIFIER=[a-zA-Z][a-zA-Z0-9:_-]*[a-zA-Z]+

EMBED_VAR="embed:" {VARIABLE}
LAYOUT_VAR="layout:" {VARIABLE}
GLOBAL_VAR=app_build|app_version|auto_log_in|build|captcha|charset|cp_url|current_id|current_page|current_path
          |current_request|current_time|current_url|debug_mode|doc_url|elapsed_time|email|error_message
          |form_declaration:wiki:edit|form_declaration:wiki:uploads|forum_build|forum_name|forum_url|group_description
          |group_id|group_title|gzip_mode|hits|homepage|include:body_extra|include:head_extra|include:spellcheck_js
          |include:theme_option_list|ip_address|ip_hostname|lang|lang:[a-zA-Z_]+|location|logged_in|logged_in_email
          |logged_in_group_description|logged_in_group_id|logged_in_ip_address|logged_in_location|logged_in_member_id
          |logged_in_private_messages|logged_in_screen_name|logged_in_total_comments|logged_in_total_entries
          |logged_in_total_forum_posts|logged_in_total_forum_topics|logged_in_username|logged_out|member_group|member_id
          |member_profile_link|module_version|page_title|path:advanced_search|path:atom|path:do_search|path:forgot
          |path:forum_home|path:image_url|path:login|path:logout|path:mark_all_read|path:memberlist|path:private_messages
          |path:recent_poster|path:register|path:rss|path:smileys|path:spellcheck_iframe|path:theme_css
          |path:view_active_topics|path:view_new_topics|path:view_pending_topics|path:wiki_base_url|path:wiki_home
          |path:your_control_panel|path:your_profile|private_messages|recent_poster|screen_name|site_id|site_index
          |site_label|site_name|site_short_name|site_url|theme_folder_url|total_comments|total_entries|total_forum_posts
          |total_forum_replies|total_forum_topics|total_queries|username|version|webmaster_email

GLOBAL_CONST=DATE_ATOM|DATE_COOKIE|DATE_ISO8601|DATE_RFC822|DATE_RFC850|DATE_RFC1036|DATE_RFC1123|DATE_RFC2822|DATE_RSS
            |DATE_W3C|XID_HASH

TAG_NAME="exp:" [a-zA-Z\-_:]+
TAG_PARAM="search:"? [a-zA-Z\-_]+ "="

// ExpressionEngine tag delimiters
LD="{"
RD="}"

// ExpressionEngine tag types
//TAG_ADDON=(exp:[a-zA-Z0-9_:]+)
//TAG_BUILTIN=(exp:(channel:(entries|calendar|month_links|next_entry|prev_entry|categories|category_archive|category_heading|info)|comment:(ajax_edit_url|edit_comment_script|entries|notification_links|subscriber_list|form|preview)|cookie_consent:message|email:(contact_form|tell_a_friend)|emoticon|file:entries|ip_to_nation:world_flags|jquery:(script_tag|script_src|output_javascript)|mailinglist:form|member:(login_form|custom_profile_data|ignore_list)|moblog:check|pages:load_site_pages|query|referrer|rss:feed|rte:script_url|forum:forum_helper|forum:topic_titles|forum|xml_encode|safecracker|search:(advanced_form|simple_form|search_results|total_results|keywords)|magpie|updated_sites:pings|stats|simple_commerce:purchase|wiki)|wiki:(categories|category_subcategories|category_articles|files|recent_changes|search_results|title_list|associated_articles|associated_pages|custom_namespaces_list))
//TAG_DEPRECATED=(exp:weblog:[^\s\}]*|exp:channel:entry_form|exp:trackback:[^\s\}]*|exp:gallery:[^\s\}]*|display_custom_fields|saef_javascript)
//TAG_GLOBAL_VAR_PARAM=(path|permalink|title_permalink|comment_path|day_path|entry_id_path|embed|encode|redirect|last_author_profile_path|member_path|member_search_path|multi_field|next_path|preload_replace:[a-zA-Z0-9_-]+|previous_path|profile_path|stylesheet|switch|thread_path|url_title_path)

// ExpressionEngine comment delimiters
COMMENT="{!--" ~"--}"

%state IN_EE_VAR
%state IN_EE_VAR_WITH_PARAM
%state IN_EE_TAG
%state IN_EE_TAG_PARAMS
%state IN_EE_EXPRESSION
%state IN_EE_CONDITIONAL
%state IN_SINGLE_STRING
%state IN_DOUBLE_STRING

%%

{WS}+ | {CRLF}                         { return TokenType.WHITE_SPACE; }
//{CRLF}                                 { return T_CRLF; }
{COMMENT}                              { return T_COMMENT; }
{LD}{WS}+                              { return T_HTML; }
{LD}{CRLF}+                            { return T_HTML; }

<YYINITIAL> {
  // Conditionals
  {LD} "if" ":elseif"? {WS} ~ {RD}     { pushState(IN_EE_EXPRESSION); yypushback(yylength() - 1); return T_LD; }
  {LD} ("/if"|"if:else") {RD}          { pushState(IN_EE_CONDITIONAL); yypushback(yylength() - 1); return T_LD; }
  // Tags
  {LD} {TAG_NAME} ~ {RD}               { pushState(IN_EE_TAG); yypushback(yylength() - 1); return T_LD; }
  {LD} "/" {TAG_NAME} {RD}             { pushState(IN_EE_TAG); yypushback(yylength() - 1); return T_LD; }
  // Variables
  {LD} {GLOBAL_VAR} {RD}               { pushState(IN_EE_VAR); yypushback(yylength() - 1); return T_LD; }
  {LD} {GLOBAL_CONST} {RD}             { pushState(IN_EE_VAR); yypushback(yylength() - 1); return T_LD; }
  {LD} {EMBED_VAR} {RD}                { pushState(IN_EE_VAR); yypushback(yylength() - 1); return T_LD; }
  {LD} {LAYOUT_VAR} {RD}               { pushState(IN_EE_VAR); yypushback(yylength() - 1); return T_LD; }
  {LD} {VARIABLE} {RD}                 { pushState(IN_EE_VAR); yypushback(yylength() - 1); return T_LD; }
  // TODO: Specify the exact variables that have a primary param
  {LD} {VARIABLE} "=" .+ {RD}          { pushState(IN_EE_VAR_WITH_PARAM); yypushback(yylength() - 1); return T_LD; }
  // TODO: This is accidentally matching {if:elseif}
  {LD} {VARIABLE} {WS} {TAG_PARAM} .+ {RD} { pushState(IN_EE_VAR_WITH_PARAM); yypushback(yylength() - 1); return T_LD; }
  // Anything else is html
  {LD} ~ {RD}                          |
  !([^]*"{"[^]*)                       {
    if (yylength() > 0) {
      return (yytext().toString().trim().length() == 0) ? TokenType.WHITE_SPACE : T_HTML;
    }
  }
}

<IN_EE_EXPRESSION> {
  "if"                                 { return T_IF; }
  "if:elseif"                          { return T_ELSEIF; }
  {RD}                                 { popState(); return T_RD; }
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
  "true"|"false"                       { return T_BOOL; }
  {NUMBER}                             { return T_NUMBER; }
  // Variables
  {GLOBAL_VAR}                         { return T_GLOBAL_VAR; }
  {GLOBAL_CONST}                       { return T_GLOBAL_CONST; }
  {EMBED_VAR}                          { return T_EMBED_VAR; }
  {LAYOUT_VAR}                         { return T_LAYOUT_VAR; }
  {VARIABLE}                           { return T_VARIABLE; }
  // Strings
  {SINGLE_QUOTE}                       { pushState(IN_SINGLE_STRING); return T_STRING_START; }
  {DOUBLE_QUOTE}                       { pushState(IN_DOUBLE_STRING); return T_STRING_START; }
  // Nested tag
//  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
}

<IN_EE_CONDITIONAL> {
  "/if"                                { return T_ENDIF; }
  "if:else"                            { return T_ELSE; }
  {RD}                                 { popState(); return T_RD; }
}

<IN_EE_VAR> {
  {RD}                                 { popState(); return T_RD; }
  // Variables
  {GLOBAL_VAR}                         { return T_GLOBAL_VAR; }
  {GLOBAL_CONST}                       { return T_GLOBAL_CONST; }
  {EMBED_VAR}                          { return T_EMBED_VAR; }
  {LAYOUT_VAR}                         { return T_LAYOUT_VAR; }
  {VARIABLE}                           { return T_VARIABLE; }
}

<IN_EE_VAR_WITH_PARAM> {
  {RD}                                 { popState(); return T_RD; }
  // Variables
  {VARIABLE}                           { pushState(IN_EE_TAG_PARAMS); return T_VARIABLE; }
}

<IN_EE_TAG> {
  {RD}                                 { popState(); return T_RD; }
  // Match tag name
  {TAG_NAME}                           { pushState(IN_EE_TAG_PARAMS); return T_TAG_NAME; }
  "/" {TAG_NAME} {RD}                  { yypushback(yylength() -1); return T_SLASH; }
}

<IN_EE_TAG_PARAMS> {
  {RD}                                 { popState(); popState(); return T_RD; }
  {TAG_PARAM}                          { yypushback(1); return T_TAG_PARAM; }
  "="                                  { return T_EQUALS; }
  // Literals
  {NUMBER}                             { return T_NUMBER; }
  {PATH}                               { return T_PATH; }
  // Variables
  {GLOBAL_VAR}                         { return T_GLOBAL_VAR; }
  {GLOBAL_CONST}                       { return T_GLOBAL_CONST; }
  {EMBED_VAR}                          { return T_EMBED_VAR; }
  {LAYOUT_VAR}                         { return T_LAYOUT_VAR; }
  {VARIABLE}                           { return T_VARIABLE; }
  // Strings
  {SINGLE_QUOTE}                       { pushState(IN_SINGLE_STRING); return T_STRING_START; }
  {DOUBLE_QUOTE}                       { pushState(IN_DOUBLE_STRING); return T_STRING_START; }
//  // Nested tag
////  {LD}                                 { pushState(IN_EE_TAG); return T_LD; }
}

<IN_SINGLE_STRING> {
//  {LD}                                 { pushState(IN_EE_TAG); return ExpressionEngineTypes.LD; }
  ((\\.)|[^'{}])+                      { return T_STRING; }
  {SINGLE_QUOTE}                       { popState(); return T_STRING_END; }
}

<IN_DOUBLE_STRING> {
//  {LD}                                 { pushState(IN_EE_TAG); return ExpressionEngineTypes.LD; }
  ((\\.)|[^\"{}])+                     { return T_STRING; }
  {DOUBLE_QUOTE}                       { popState(); return T_STRING_END; }
}

.                                      { return TokenType.BAD_CHARACTER; }
