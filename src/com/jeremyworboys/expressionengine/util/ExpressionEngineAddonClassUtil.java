package com.jeremyworboys.expressionengine.util;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineAddonClassUtil {
    @NotNull
    public static String getAccessoryClassName(@NotNull String addonName) {
        return StringUtil.capitalize(addonName) + "_acc";
    }

    @NotNull
    public static String getExtensionClassName(@NotNull String addonName) {
        return StringUtil.capitalize(addonName) + "_ext";
    }

    @NotNull
    public static String getFieldTypeClassName(@NotNull String addonName) {
        // TODO: Field types aren't necessarily named after their addon
        return StringUtil.capitalize(addonName) + "_ft";
    }

    @NotNull
    public static String getPluginClassName(@NotNull String addonName) {
        return StringUtil.capitalize(addonName);
    }

    @NotNull
    public static String getModuleClassName(@NotNull String addonName) {
        return StringUtil.capitalize(addonName);
    }

    @NotNull
    public static String getModuleControlPanelClassName(@NotNull String addonName) {
        return StringUtil.capitalize(addonName) + "_mcp";
    }

    @NotNull
    public static String getModuleTabClassName(@NotNull String addonName) {
        return StringUtil.capitalize(addonName) + "_tab";
    }

    @NotNull
    public static String getModuleUpdaterClassName(@NotNull String addonName) {
        return StringUtil.capitalize(addonName) + "_upd";
    }

    @NotNull
    public static String getAddonClassName(@NotNull String addonName, @NotNull String addonPrefix) {
        switch (addonPrefix) {
            case "acc":
                return getAccessoryClassName(addonName);
            case "ext":
                return getExtensionClassName(addonName);
            case "ft":
                return getFieldTypeClassName(addonName);
            case "pi":
                return getPluginClassName(addonName);
            case "mod":
                return getModuleClassName(addonName);
            case "mcp":
                return getModuleControlPanelClassName(addonName);
            case "tab":
                return getModuleTabClassName(addonName);
            case "upd":
                return getModuleUpdaterClassName(addonName);
            default:
                throw new IllegalStateException("Unrecognised state " + addonPrefix);
        }
    }
}
