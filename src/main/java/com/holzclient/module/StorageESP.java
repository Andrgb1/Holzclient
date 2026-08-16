package com.holzclient.module;

import com.holzclient.Config;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class StorageESP extends Module {
    private final Set<BlockPos> found = Collections.synchronizedSet(new HashSet<>());
    private boolean scanning = false;

    public StorageESP() { super("StorageESP"); }

    @Override
    protected void onEnable() { startScan(); }

    @Override
    protected void onDisable() { found.clear(); }

    private boolean isStorageBlock(Block b) {
        return b == Blocks.CHEST || b == Blocks.TRAPPED_CHEST || b == Blocks.BARREL ||
                b == Blocks.SHULKER_BOX || b == Blocks.HOPPER;
    }

    private void startScan() {
        if (scanning) return;
        scanning = true;
        CompletableFuture.runAsync(() -> {
            try {
                MinecraftClient mc = MinecraftClient.getInstance();
                while (isEnabled()) {
                    if (mc.player == null || mc.world == null) { Thread.sleep(1000); continue; }
                    BlockPos p = mc.player.getBlockPos();
                    Set<BlockPos> newFound = new HashSet<>();
                    int r = 48;
                    for (int x = -r; x <= r; x++) {
                        for (int y = -8; y <= 8; y++) {
                            for (int z = -r; z <= r; z++) {
                                BlockPos pos = p.add(x, y, z);
                                Block b = mc.world.getBlockState(pos).getBlock();
                                if (isStorageBlock(b)) newFound.add(pos);
                            }
                        }
                    }
                    found.clear();
                    found.addAll(newFound);
                    Thread.sleep(2500);
                }
            } catch (Exception ignored) {}
            scanning = false;
        });
    }

    @Override
    public void onWorldRender(MinecraftClient client, float tickDelta) {
        synchronized (found) {
            for (BlockPos pos : found) {
                client.world.addParticle(ParticleTypes.END_ROD,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        0, 0.05, 0);
            }
        }
    }

    @Override
    public void tick(MinecraftClient client) {
        if (client.player == null) return;
        if (found.size() > 0) {
            client.player.sendMessage(net.minecraft.text.Text.of("StorageESP: " + found.size() + " gefunden"), true);
        }
    }
}
