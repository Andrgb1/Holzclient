package com.holzclient.module;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class BlockNotifier extends Module {
    private Block target = null;
    private int radius = 16;

    public BlockNotifier() { super("BlockNotifier"); }

    public void setTarget(Block b) { this.target = b; }

    @Override
    public void tick(MinecraftClient client) {
        if (client.player == null || client.world == null || target == null) return;
        BlockPos p = client.player.getBlockPos();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = p.add(x,y,z);
                    if (client.world.getBlockState(pos).getBlock() == target) {
                        client.player.sendMessage(net.minecraft.text.Text.of("BlockNotifier: " + target.getTranslationKey() + " at " + pos.toShortString()), false);
                        setEnabled(false);
                        return;
                    }
                }
            }
        }
    }
}
