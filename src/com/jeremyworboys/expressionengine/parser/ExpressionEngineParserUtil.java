package com.jeremyworboys.expressionengine.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes.*;

public class ExpressionEngineParserUtil extends GeneratedParserUtilBase {
    public static boolean checkPairVariable(PsiBuilder builder, int level, boolean pair) {
        if (builder.getTokenType() != T_LD) {
            return false;
        }

        // Set a marker that so the parser can be rolled back to the start of the tag
        PsiBuilder.Marker marker = builder.mark();
        String variableName;

        // Capture this module's name
        consumeTokenFast(builder, T_LD);
        if (nextTokenIsFast(builder, T_VARIABLE_NAME)) {
            variableName = builder.getTokenText();
            assert variableName != null;
        } else {
            return false;
        }

        // Advance lexer increasing depth each time an open module tag with this name is found and
        // decreasing depth each time a closing module tag with this name is found until the original
        // module is closed or the eof is reached.
        builder.advanceLexer();
        int depth = 0;
        while (depth >= 0 && !builder.eof()) {
            if (nextTokenIsFast(builder, T_LD)) {
                builder.advanceLexer();
                if (nextTokenIsFast(builder, T_SLASH)) {
                    builder.advanceLexer();
                    if (nextTokenIsFast(builder, T_VARIABLE_NAME) && variableName.equals(builder.getTokenText())) {
                        --depth;
                    }
                } else if (nextTokenIsFast(builder, T_VARIABLE_NAME) && variableName.equals(builder.getTokenText())) {
                    ++depth;
                }
            } else {
                builder.advanceLexer();
            }
        }

        marker.rollbackTo();

        // Depth will be -1 if this module tag was closed
        return (depth < 0) == pair;
    }

    public static boolean checkPairModule(PsiBuilder builder, int level, boolean pair) {
        if (builder.getTokenType() != T_LD) {
            return false;
        }

        // Set a marker that so the parser can be rolled back to the start of the tag
        PsiBuilder.Marker marker = builder.mark();
        String moduleName;

        // Capture this module's name
        consumeTokenFast(builder, T_LD);
        if (nextTokenIsFast(builder, T_MODULE_NAME)) {
            moduleName = builder.getTokenText();
            assert moduleName != null;
        } else {
            return false;
        }

        // Advance lexer increasing depth each time an open module tag with this name is found and
        // decreasing depth each time a closing module tag with this name is found until the original
        // module is closed or the eof is reached.
        builder.advanceLexer();
        int depth = 0;
        while (depth >= 0 && !builder.eof()) {
            if (nextTokenIsFast(builder, T_LD)) {
                builder.advanceLexer();
                if (nextTokenIsFast(builder, T_SLASH)) {
                    builder.advanceLexer();
                    if (nextTokenIsFast(builder, T_MODULE_NAME) && moduleName.equals(builder.getTokenText())) {
                        --depth;
                    }
                } else if (nextTokenIsFast(builder, T_MODULE_NAME) && moduleName.equals(builder.getTokenText())) {
                    ++depth;
                }
            } else {
                builder.advanceLexer();
            }
        }

        marker.rollbackTo();

        // Depth will be -1 if this module tag was closed
        return (depth < 0) == pair;
    }
}
