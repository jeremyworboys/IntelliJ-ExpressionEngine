package com.jeremyworboys.expressionengine.template.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ResourceUtil;
import com.jeremyworboys.expressionengine.template.ExpressionEngineFileType;
import com.jeremyworboys.expressionengine.template.ExpressionEngineLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class ExpressionEngineColorSettingsPage implements ColorSettingsPage {
    private static final SyntaxHighlighter SYNTAX_HIGHLIGHTER;
    private static final AttributesDescriptor[] DESCRIPTORS;
    private static final String DEMO_TEXT;

    @Nullable
    @Override
    public Icon getIcon() {
        return ExpressionEngineFileType.INSTANCE.getIcon();
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return SYNTAX_HIGHLIGHTER;
    }

    @NotNull
    @Override
    public String getDemoText() {
        return DEMO_TEXT;
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

    static {
        SYNTAX_HIGHLIGHTER = SyntaxHighlighterFactory.getSyntaxHighlighter(ExpressionEngineLanguage.INSTANCE, null, null);
        DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Comment", ExpressionEngineHighlighter.COMMENT),
            new AttributesDescriptor("Number", ExpressionEngineHighlighter.NUMBER),
            new AttributesDescriptor("Operator", ExpressionEngineHighlighter.OPERATOR),
            new AttributesDescriptor("String", ExpressionEngineHighlighter.STRING),
            new AttributesDescriptor("Tag", ExpressionEngineHighlighter.TAG),
            new AttributesDescriptor("Tag Parameter", ExpressionEngineHighlighter.TAG_PARAM),
        };

        try {
            String e = ResourceUtil.loadText(ExpressionEngineColorSettingsPage.class.getResource("/template/sample_text.html"));
            DEMO_TEXT = StringUtil.convertLineSeparators(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
