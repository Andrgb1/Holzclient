package com.holzclient;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyBinding TOGGLE_GUI;

    public static void register() {
        TOGGLE_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.holzclient.toggle_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.holzclient.main"
        ));
    }
}
