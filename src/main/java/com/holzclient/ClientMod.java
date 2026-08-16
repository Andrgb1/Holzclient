package com.holzclient;

import com.holzclient.module.ModuleManager;
import com.holzclient.ui.HolzClientScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;

public class ClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Keybinds.register();
        ModuleManager.init();
        ModuleManager.loadConfig();

        // Setup tick: handle keybind + module ticks
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (Keybinds.TOGGLE_GUI.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new HolzClientScreen());
                else client.setScreen(null);
            }
            ModuleManager.tickAll(client);
        });

        // World render hook so modules can render highlights
        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            ModuleManager.renderAll(mc, context.tickDelta());
        });
    }
}
