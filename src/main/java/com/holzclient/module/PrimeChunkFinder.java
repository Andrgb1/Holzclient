package com.holzclient.module;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PrimeChunkFinder extends Module {
    private final Set<ChunkPos> found = new HashSet<>();
    private boolean scanning = false;

    public PrimeChunkFinder() { super("PrimeChunkFinder"); }

    @Override
    protected void onEnable() { startScan(); }
    @Override
    protected void onDisable() { found.clear(); }

    private void startScan() {
        if (scanning) return;
        scanning = true;
        CompletableFuture.runAsync(() -> {
            try {
                MinecraftClient mc = MinecraftClient.getInstance();
                while (isEnabled()) {
                    if (mc.player == null || mc.world == null) { Thread.sleep(1000); continue; }
                    ChunkPos center = new ChunkPos(mc.player.getBlockPos());
                    int r = 6;
                    Set<ChunkPos> newFound = new HashSet<>();
                    for (int cx = center.x - r; cx <= center.x + r; cx++) {
                        for (int cz = center.z - r; cz <= center.z + r; cz++) {
                            boolean has = false;
                            for (int x = 0; x < 16 && !has; x++) {
                                for (int z = 0; z < 16 && !has; z++) {
                                    for (int y = 0; y < 256; y++) {
                                        BlockPos pos = new BlockPos(cx*16 + x, y, cz*16 + z);
                                        if (mc.world.getBlockState(pos).getBlock() == Blocks.DEEPSLATE_COAL_ORE ||
                                            mc.world.getBlockState(pos).getBlock() == Blocks.DEEPSLATE_IRON_ORE ||
                                            mc.world.getBlockState(pos).getBlock() == Blocks.DEEPSLATE_GOLD_ORE) {
                                            has = true; break;
                                        }
                                    }
                                }
                            }
                            if (has) newFound.add(new ChunkPos(cx, cz));
                        }
                    }
                    found.clear();
                    found.addAll(newFound);
                    Thread.sleep(4500);
                }
            } catch (Exception ignored) {}
            scanning = false;
        });
    }

    @Override
    public void tick(MinecraftClient client) {
        if (!found.isEmpty() && client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.of("PrimeChunkFinder: " + found.size() + " chunks"), true);
        }
    }
}
