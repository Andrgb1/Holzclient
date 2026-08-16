package com.holzclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerESP extends Module {
    public PlayerESP() { super("PlayerESP"); }

    @Override
    public void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;
        for (PlayerEntity p : client.world.getPlayers()) {
            if (p.getUuid().equals(client.player.getUuid())) continue;
            double dx = p.getX() - client.player.getX();
            double dz = p.getZ() - client.player.getZ();
            double dist = Math.sqrt(dx*dx + dz*dz);
            client.player.sendMessage(net.minecraft.text.Text.of("Player: " + p.getEntityName() + " d=" + (int)dist), true);
        }
    }
}
