package com.holzclient.module;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AncientDebrisFinder extends Module {
    private final Set<BlockPos> found = new HashSet<>();
    private boolean scanning = false;

    public AncientDebrisFinder() { super("AncientDebrisFinder"); }

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
                    if (!mc.world.getRegistryKey().getValue().toString().contains("the_nether")) { Thread.sleep(2000); continue; }
                    BlockPos p = mc.player.getBlockPos();
                    Set<BlockPos> newF = new HashSet<>();
                    int r = 40;
                    for (int x=-r; x<=r; x++) for (int y=-20; y<=108; y++) for (int z=-r; z<=r; z++) {
                        BlockPos pos = p.add(x,y,z);
                        if (mc.world.getBlockState(pos).getBlock() == Blocks.ANCIENT_DEBRIS) newF.add(pos);
                    }
                    found.clear(); found.addAll(newF);
                    Thread.sleep(3000);
                }
            } catch (Exception ignored) {}
            scanning = false;
        });
    }

    @Override
    public void tick(MinecraftClient client) {
        if (!found.isEmpty() && client.player != null) client.player.sendMessage(net.minecraft.text.Text.of("AncientDebris: " + found.size()), true);
    }
}
