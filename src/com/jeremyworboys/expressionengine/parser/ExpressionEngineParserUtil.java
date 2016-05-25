package com.jeremyworboys.expressionengine.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;

import static com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes.*;

public class ExpressionEngineParserUtil extends GeneratedParserUtilBase {
    public static boolean checkPairVariable(PsiBuilder builder, int level, boolean pair) {
        return checkPairTag(T_VARIABLE_NAME, builder, pair);
    }

    public static boolean checkPairModule(PsiBuilder builder, int level, boolean pair) {
        return checkPairTag(T_MODULE_NAME, builder, pair);
    }

    private static boolean checkPairTag(IElementType tagNameType, PsiBuilder builder, boolean pair) {
        if (builder.getTokenType() != T_LD) {
            return false;
        }

        // Set a marker that so the parser can be rolled back to the start of the tag
        PsiBuilder.Marker marker = builder.mark();
        String tagName;

        // Capture this tag's name
        consumeTokenFast(builder, T_LD);
        if (nextTokenIsFast(builder, tagNameType)) {
            tagName = builder.getTokenText();
            assert tagName != null;
        } else {
            return false;
        }

        // Advance lexer increasing depth each time an open tag with this name is found and
        // decreasing depth each time a closing tag with this name is found until the original
        // tag is closed or the eof is reached.
        builder.advanceLexer();
        int depth = 0;
        while (depth >= 0 && !builder.eof()) {
            if (nextTokenIsFast(builder, T_LD)) {
                builder.advanceLexer();
                if (nextTokenIsFast(builder, T_SLASH)) {
                    builder.advanceLexer();
                    if (nextTokenIsFast(builder, tagNameType) && tagName.equals(builder.getTokenText())) {
                        --depth;
                    }
                } else if (nextTokenIsFast(builder, tagNameType) && tagName.equals(builder.getTokenText())) {
                    ++depth;
                }
            } else {
                builder.advanceLexer();
            }
        }

        marker.rollbackTo();

        // Depth will be -1 if this tag was closed
        return (depth < 0) == pair;
    }
}
