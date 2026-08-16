package com.holzclient.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class XRayModule extends Module {
    private final Set<BlockPos> ores = Collections.synchronizedSet(new HashSet<>());
    private boolean scanning = false;

    public XRayModule() { super("XRay"); }

    private boolean isTarget(Block b) {
        return b == Blocks.DIAMOND_ORE || b == Blocks.GOLD_ORE || b == Blocks.IRON_ORE ||
               b == Blocks.ANCIENT_DEBRIS || b == Blocks.REDSTONE_ORE || b == Blocks.LAPIS_ORE;
    }

    @Override
    protected void onEnable() { startScan(); }

    @Override
    protected void onDisable() { ores.clear(); }

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
                    int r = 40;
                    for (int x = -r; x <= r; x++) {
                        for (int y = Math.max(0, p.getY() - 20); y <= Math.min(255, p.getY() + 20); y++) {
                            for (int z = -r; z <= r; z++) {
                                BlockPos pos = p.add(x, y, z);
                                Block b = mc.world.getBlockState(pos).getBlock();
                                if (isTarget(b)) newFound.add(pos);
                            }
                        }
                    }
                    ores.clear();
                    ores.addAll(newFound);
                    Thread.sleep(3000);
                }
            } catch (Exception ignored) {}
            scanning = false;
        });
    }

    @Override
    public void onWorldRender(MinecraftClient client, float tickDelta) {
        synchronized (ores) {
            for (BlockPos pos : ores) {
                client.world.addParticle(ParticleTypes.COMPOSTER, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0,0,0);
            }
        }
    }
}
