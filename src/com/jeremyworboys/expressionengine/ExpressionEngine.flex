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
TAG_ADDON=(exp:[a-zA-Z0-9_:]+)
TAG_BUILTIN=(exp:(channel:(entries|calendar|month_links|next_entry|prev_entry|categories|category_archive|category_heading|info)|comment:(ajax_edit_url|edit_comment_script|entries|notification_links|subscriber_list|form|preview)|cookie_consent:message|email:(contact_form|tell_a_friend)|emoticon|file:entries|ip_to_nation:world_flags|jquery:(script_tag|script_src|output_javascript)|mailinglist:form|member:(login_form|custom_profile_data|ignore_list)|moblog:check|pages:load_site_pages|query|referrer|rss:feed|rte:script_url|forum:forum_helper|forum:topic_titles|forum|xml_encode|safecracker|search:(advanced_form|simple_form|search_results|total_results|keywords)|magpie|updated_sites:pings|stats|simple_commerce:purchase|wiki)|wiki:(categories|category_subcategories|category_articles|files|recent_changes|search_results|title_list|associated_articles|associated_pages|custom_namespaces_list))
TAG_DEPRECATED=(exp:weblog:[^\s\}]*|exp:channel:entry_form|exp:trackback:[^\s\}]*|exp:gallery:[^\s\}]*|display_custom_fields|saef_javascript)
TAG_CONSTANT=(DATE_ATOM|DATE_COOKIE|DATE_ISO8601|DATE_RFC822|DATE_RFC850|DATE_RFC1036|DATE_RFC1123|DATE_RFC2822|DATE_RSS|DATE_W3C|XID_HASH)
TAG_GLOBAL_VAR=(app_build|app_version|auto_log_in|build|captcha|charset|cp_url|current_id|current_page|current_path|current_request|current_time|current_url|debug_mode|doc_url|elapsed_time|email|error_message|form_declaration:wiki:edit|form_declaration:wiki:uploads|forum_build|forum_name|forum_url|group_description|group_id|group_title|gzip_mode|hits|homepage|include:body_extra|include:head_extra|include:spellcheck_js|include:theme_option_list|ip_address|ip_hostname|lang|lang:[a-zA-Z_]+|location|logged_in|logged_in_email|logged_in_group_description|logged_in_group_id|logged_in_ip_address|logged_in_location|logged_in_member_id|logged_in_private_messages|logged_in_screen_name|logged_in_total_comments|logged_in_total_entries|logged_in_total_forum_posts|logged_in_total_forum_topics|logged_in_username|logged_out|member_group|member_id|member_profile_link|module_version|page_title|path:advanced_search|path:atom|path:do_search|path:forgot|path:forum_home|path:image_url|path:login|path:logout|path:mark_all_read|path:memberlist|path:private_messages|path:recent_poster|path:register|path:rss|path:smileys|path:spellcheck_iframe|path:theme_css|path:view_active_topics|path:view_new_topics|path:view_pending_topics|path:wiki_base_url|path:wiki_home|path:your_control_panel|path:your_profile|private_messages|recent_poster|screen_name|site_id|site_index|site_label|site_name|site_short_name|site_url|theme_folder_url|total_comments|total_entries|total_forum_posts|total_forum_replies|total_forum_topics|total_queries|username|version|webmaster_email)
TAG_GLOBAL_VAR_PARAM=(path|permalink|title_permalink|comment_path|day_path|entry_id_path|embed|encode|redirect|last_author_profile_path|member_path|member_search_path|multi_field|next_path|preload_replace:[a-zA-Z0-9_-]+|previous_path|profile_path|stylesheet|switch|thread_path|url_title_path)

// ExpressionEngine conditionals
ELSE_IF="if:elseif"
ELSE="if:else"
IF="if"
END_IF="/if"

// ExpressionEngine comment delimiters
COMMENT="{!--" ~"--}"

%state IN_EE_TAG
%state IN_SINGLE_STRING
%state IN_DOUBLE_STRING

%%

<YYINITIAL> {
  {LD}                                 { pushState(IN_EE_TAG); return ExpressionEngineTypes.LD; }
  {COMMENT}                            { return ExpressionEngineTypes.COMMENT; }

  {CRLF}                               { return ExpressionEngineTypes.CRLF; }
  {WS}+                                { return TokenType.WHITE_SPACE; }
}

<IN_EE_TAG> {
  {RD}                                 { popState(); return ExpressionEngineTypes.RD; }

  {COMMENT}                            { return ExpressionEngineTypes.COMMENT; }

  {ELSE_IF}                            { return ExpressionEngineTypes.ELSE_IF; }
  {ELSE}                               { return ExpressionEngineTypes.ELSE; }
  {IF}                                 { return ExpressionEngineTypes.IF; }
  {END_IF}                             { return ExpressionEngineTypes.END_IF; }

  {TAG_BUILTIN}                        { return ExpressionEngineTypes.TAG_BUILTIN; }
  {TAG_ADDON}                          { return ExpressionEngineTypes.TAG_ADDON; }
  {TAG_CONSTANT}                       { return ExpressionEngineTypes.TAG_CONSTANT; }
  {TAG_DEPRECATED}                     { return ExpressionEngineTypes.TAG_DEPRECATED; }
  {TAG_GLOBAL_VAR}                     { return ExpressionEngineTypes.TAG_GLOBAL_VAR; }
  {TAG_GLOBAL_VAR_PARAM}               { return ExpressionEngineTypes.TAG_GLOBAL_VAR_PARAM; }

  "/" {TAG_BUILTIN}                    { return ExpressionEngineTypes.TAG_BUILTIN_CLOSE; }
  "/" {TAG_ADDON}                      { return ExpressionEngineTypes.TAG_ADDON_CLOSE; }
  "/" {TAG_DEPRECATED}                 { return ExpressionEngineTypes.TAG_DEPRECATED_CLOSE; }

  {IDENTIFIER}                         { return ExpressionEngineTypes.IDENTIFIER; }

  {EQUAL}                              { return ExpressionEngineTypes.EQUAL; }
  {NUMBER}                             { return ExpressionEngineTypes.NUMBER; }
  {SINGLE_QUOTE}                       { pushState(IN_SINGLE_STRING); return ExpressionEngineTypes.STRING_START; }
  {DOUBLE_QUOTE}                       { pushState(IN_DOUBLE_STRING); return ExpressionEngineTypes.STRING_START; }

  {CRLF}                               { return ExpressionEngineTypes.CRLF; }
  {WS}+                                { return TokenType.WHITE_SPACE; }
}

<IN_SINGLE_STRING> {
  {LD}                                 { pushState(IN_EE_TAG); return ExpressionEngineTypes.LD; }
  {COMMENT}                            { return ExpressionEngineTypes.COMMENT; }
  ((\\.)|[^'{}])+                      { return ExpressionEngineTypes.STRING; }
  {SINGLE_QUOTE}                       { popState(); return ExpressionEngineTypes.STRING_END; }
}

<IN_DOUBLE_STRING> {
  {LD}                                 { pushState(IN_EE_TAG); return ExpressionEngineTypes.LD; }
  {COMMENT}                            { return ExpressionEngineTypes.COMMENT; }
  ((\\.)|[^\"{}])+                     { return ExpressionEngineTypes.STRING; }
  {DOUBLE_QUOTE}                       { popState(); return ExpressionEngineTypes.STRING_END; }
}

.                                      { return ExpressionEngineTypes.HTML; }
