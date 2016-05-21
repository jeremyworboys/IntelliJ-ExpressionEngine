package com.jeremyworboys.expressionengine.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.jeremyworboys.expressionengine.icons.ExpressionEngineIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class ExpressionEngineColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
        new AttributesDescriptor("Comment", ExpressionEngineSyntaxHighlighter.COMMENT),
        new AttributesDescriptor("Identifier", ExpressionEngineSyntaxHighlighter.IDENTIFIER),
        new AttributesDescriptor("Number", ExpressionEngineSyntaxHighlighter.NUMBER),
        new AttributesDescriptor("Operator", ExpressionEngineSyntaxHighlighter.OPERATOR),
        new AttributesDescriptor("String", ExpressionEngineSyntaxHighlighter.STRING),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return ExpressionEngineIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ExpressionEngineSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        // TODO: Load from external file
        return "{!-- this is a comment --}\n" +
            "{exp:channel:entries channel=\"homepage\" limit=1 dynamic=\"off\" require_entry=\"yes\"}\n" +
            "    {if no_results}{redirect=\"404\"}{/if}\n" +
            "    <div class=\"home-content\">\n" +
            "        <h1>{home_content_title}</h1>\n" +
            "        {if home_content_intro != \"\"}" +
            "            <p class=\"home-content-intro\">{home_content_intro}</p>\n" +
            "        {/if}" +
            "        <p>{home_content_body}</p>\n" +
            "        <a href=\"{home_button_location}\" class=\"btn btn-brand\">{home_button_text}</a>\n" +
            "    </div>\n" +
            "{/exp:channel:entries}";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        // TODO: Add additional highlighter tags
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "ExpressionEngine";
    }
}
