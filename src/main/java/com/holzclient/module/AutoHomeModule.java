package com.holzclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class AutoHomeModule extends Module {
    private boolean hasRun = false;

    public AutoHomeModule() { super("AutoHome"); }

    @Override
    public void tick(MinecraftClient client) {
        if (client.player == null) return;
        BlockPos p = client.player.getBlockPos();
        if (!hasRun && p.getY() <= -5) {
            client.player.sendChatMessage("/delhome 3");
            client.player.sendChatMessage("/sethome 3");
            client.player.sendChatMessage("/rtp");
            client.player.sendChatMessage("/home 3");
            hasRun = true;
        }
        if (hasRun && p.getY() > -4) hasRun = false;
    }
}
