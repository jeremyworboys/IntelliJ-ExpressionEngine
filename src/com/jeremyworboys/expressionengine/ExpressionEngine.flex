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
TAG_GLOBAL_VAR=(app_build|app_version|auto_log_in|build|captcha|charset|cp_url|current_id|current_page|current_path|current_request|current_time|current_url|debug_mode|doc_url|elapsed_time|email|error_message|form_declaration:wiki:edit|form_declaration:wiki:uploads|forum_build|forum_name|forum_url|group_description|group_id|group_title|gzip_mode|hits|homepage|include:body_extra|include:head_extra|include:spellcheck_js|include:theme_option_list|ip_address|ip_hostname|lang|lang:[a-zA-Z_]+|location|logged_in|logged_in_email|logged_in_group_description|logged_in_group_id|logged_in_ip_address|logged_in_location|logged_in_member_id|logged_in_private_messages|logged_in_screen_name|logged_in_total_comments|logged_in_total_entries|logged_in_total_forum_posts|logged_in_total_forum_topics|logged_in_username|logged_out|member_group|member_id|member_profile_link|module_version|page_title|path:advanced_search|path:atom|path:do_search|path:forgot|path:forum_home|path:image_url|path:login|path:logout|path:mark_all_read|path:memberlist|path:private_messages|path:recent_poster|path:register|path:rss|path:smileys|path:spellcheck_iframe|path:theme_css|path:view_active_topics|path:view_new_topics|path:view_pending_topics|path:wiki_base_url|path:wiki_home|path:your_control_panel|path:your_profile|private_messages|recent_poster|screen_name|site_id|site_index|site_label|site_name|site_short_name|site_url|theme_folder_url|total_comments|total_entries|total_forum_posts|total_forum_replies|total_forum_topics|total_queries|username|version|webmaster_email)
TAG_GLOBAL_VAR_PARAM=(path|permalink|title_permalink|comment_path|day_path|entry_id_path|embed|encode|redirect|last_author_profile_path|member_path|member_search_path|multi_field|next_path|preload_replace:[a-zA-Z0-9_-]+|previous_path|profile_path|stylesheet|switch|thread_path|url_title_path)
TAG_LOCAL_VAR=(\/?add_to_cart_button|\/?articles|\/?audio|\/?buy_now_button|\/?calendar_heading|\/?calendar_rows|\/?categories|\/?category_menu|\/?custom_fields|\/?date_footer|\/?date_heading|\/?entries|\/?entry_titles|\/?field|\/?field_errors|\/?files|\/?first_page|\/?footer|\/?global_errors|\/?header|\/?images|\/?items|\/?last_page|\/?letter_header|\/?member_names|\/?moderators|\/?movie|\/?next_page|\/?options|\/?options:[a-zA-Z0-9_]+|\/?page|\/?paginate|\/?pagination_links|\/?parents|\/?ping_row|\/?ping_servers|\/?preview|\/?previous_page|\/?row_blank|\/?row_column|\/?row_end|\/?row_start|\/?siblings|\/?status|\/?status_footer|\/?status_group|\/?status_header|\/?status_menu|\/?statuses|\/?sticky|\/?view_cart_button|\/?year_heading|[a-zA-Z0-9_]+_file_url|absolute_count|absolute_results|active|add_to_cart_url|address_city|address_country|address_name|address_state|address_status|address_street|address_zip|age|allow_comments|allow_multiple|aol_im|article|article_history|article_title|articles_total|author|author_email|author_id|author_name|auto_login|auto_path|auto_thread_path|avatar|avatar_height|avatar_image_height|avatar_image_width|avatar_url|avatar_width|bio|birthday|blank|board_label|board_name|body|bulletins|buy_now_url|can_admin|can_ban|can_change_status|can_delete|can_edit|can_merge|can_moderate_comment|can_move|can_post|can_post_bulletin|can_private_message|can_report|can_split|cannot_admin|cannot_edit|captcha|caption|category_description|category_group|category_id|category_image|category_name|category_namespace|category_request|category_url_title|channel|channel_description|channel_encoding|channel_id|channel_lang|channel_language|channel_name|channel_names|channel_short_name|channel_title|channel_url|checkbox|children|class|clear_all_cookies_link|clear_ee_cookies_link|comment|comment_auto_path|comment_date|comment_entry_id_auto_path|comment_id|comment_site_id|comment_stripped|comment_subscriber_total|comment_tb_total|comment_total|comment_url_title_auto_path|comments_disabled|comments_expired|content|cookies_allowed|cookies_allowed_link|cookies_not_allowed|count|credit|date|day_number|depth|description|directory_id|directory_title|display_field|edit_article|edit_date|editable|email|empty_feed|encoding|entry_author_id|entry_date|entry_id|entry_site_id|error|error:[a-zA-Z0-9_]+|excerpt|expiration_date|extended_description|extension|field_data|field_errors:count|field_id|field_instructions|field_label|field_name|file_id|file_name|file_namespace|file_page|file_size|file_type|file_types|file_url|filename|file|first_child|first_name|formatting_buttons|forum_topic|forum_topic_id|forums_exist|freelancer_version|full_name|full_text|gender|global_errors:count|gmt_comment_date|gmt_date|gmt_edit_date|gmt_entry_date|gmt_last_updated|gmt_revision_date|group_description|group_id|heading|height|history|html_email|icq|id_auto_path|id_path|ignore_screen_name|image_namespace|in_forum|instructions|instructions:[a-zA-Z0-9_]+|interests|ip_address|is_ignored|is_image|is_post|is_topic|item|item_enabled|item_id|item_name|item_purchases|item_regular_price|item_sale_price|item_type|item_use_sale|join_date|keywords|label:[a-zA-Z0-9_]+|language|last_activity|last_author|last_child|last_comment_date|last_entry_date|last_forum_post_date|last_name|last_post_date|last_post_id|last_post_relative_date|last_reply|last_segment|last_updated|last_visit|last_visitor_date|letter|link|local_time|location|locked|mailing_list|main_namespace|maxlength|member_email|member_name|message_text|meta_refresh|mime_type|moderated|modified_date|month|month_num|month_short|most_visitor_date|most_visitors|msn_im|multiselect|name|name_namespace|namespace|namespace_label|new_article|next:|next_date|next_topic|next_topic_title|no_bulletins|no_history|no_results|no_results_page|not_category_request|not_entries|not_forum_topic|notes|occupation|old_revision|option_name|option_value|original_page|page_id|page_uri|page_url|pagination_page_number|pagination_url|parent_id|parents:|parents:title|parents:url-title|password|path|path:author_profile|path:close_revision|path:edit_topic|path:open_revision|path:redirect_page|path:topic_history|path:view_article|path:view_category|path:view_file|path:view_orig_article|path:view_revision|path:wiki_base_url|payer_business_name|payer_status|payment_fee|payment_gross|permalink|photo|photo_height|photo_image_height|photo_image_width|photo_url|photo_width|ping_date|ping_ipaddress|ping_server_name|ping_site_check|ping_site_name|ping_site_rss|ping_site_url|plain_email|post_date|post_number|post_total|posts|previous:|previous_date|previous_topic|previous_topic_title|private_messages|quantity|radio|recent_comment_date|redirect_page|redirected|ref_agent|ref_agent_short|ref_date|ref_from|ref_ip|ref_to|relationship|relative_date|relative_url|replies|reply_author|reply_results|required|results|revision|revision_author|revision_date|revision_id|revision_notes|revision_status|row_end|row_start|rows|safecracker_file|safecracker_head|screen_name|search_path|segment_[0-9]+|select|select_date|select_options|selected|selected_option:[a-zA-Z0-9_]+(:label)?|send_private_message|siblings:|siblings:title|siblings:url-title|signature|signature_image|signature_image_height|signature_image_url|signature_image_width|size|smiley|special_namespace|special_page|subcategory_total|subscribed|subscriber_count|subscriber_email|subscriber_guest_total|subscriber_is_member|subscriber_member_id|subscriber_member_total|subscriber_screen_name|subscriber_total_results|summary|switch|template_edit_date|text|text_direction|textarea|textinput|timezone|title|title_permalink|topic_date|topic_relative_date|topic_title|topic_type|total_anon|total_comments|total_entries|total_forum_posts|total_forum_topics|total_guests|total_logged_in|total_members|total_pages|total_posts|total_replies|total_results|trackback_total|trimmed_url|unsubscribe_url|upload_date|uploads|url|url:[a-zA-Z0-9_]+|url_as_author|url_or_email|url_or_email_as_author|url_or_email_as_link|url_title|username|version|view_cart_url|view_count_four|view_count_one|view_count_three|view_count_two|viewable_image|views|week_date|width|wiki_name|yahoo_im|year|year_short)

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

  {TAG_BUILTIN}                        { return ExpressionEngineTypes.TAG_BUILTIN; }
  {TAG_ADDON}                          { return ExpressionEngineTypes.TAG_ADDON; }
  {TAG_CONSTANT}                       { return ExpressionEngineTypes.TAG_CONSTANT; }
  {TAG_DEPRECATED}                     { return ExpressionEngineTypes.TAG_DEPRECATED; }
  {TAG_GLOBAL_VAR}                     { return ExpressionEngineTypes.TAG_GLOBAL_VAR; }
  {TAG_GLOBAL_VAR_PARAM}               { return ExpressionEngineTypes.TAG_GLOBAL_VAR_PARAM; }
  {TAG_LOCAL_VAR}                      { return ExpressionEngineTypes.TAG_LOCAL_VAR; }
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
