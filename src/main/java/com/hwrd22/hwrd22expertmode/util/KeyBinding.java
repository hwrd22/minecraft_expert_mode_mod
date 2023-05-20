package com.hwrd22.hwrd22expertmode.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_METERS = "key.category.hwrd22expertmode.meters";
    public static final String KEY_USE_RAGE = "key.hwrd22expertmode.rage";
    public static final String KEY_USE_ADRENALINE = "key.hwrd22expertmode.adrenaline";

    public static final KeyMapping RAGE_KEY = new KeyMapping(KEY_USE_RAGE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_METERS);
    public static final KeyMapping ADRENALINE_KEY = new KeyMapping(KEY_USE_ADRENALINE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_METERS);
}
