package com.holzclient.module;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    protected final String name;
    protected boolean enabled = false;

    public Module(String name) { this.name = name; }
    public String getName() { return name; }
    public boolean isEnabled() { return enabled; }

    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean e) {
        if (e == enabled) return;
        enabled = e;
        if (enabled) onEnable();
        else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public void tick(MinecraftClient client) {}
    public void onWorldRender(MinecraftClient client, float tickDelta) {}
}
