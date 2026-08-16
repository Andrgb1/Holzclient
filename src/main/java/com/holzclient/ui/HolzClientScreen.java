package com.holzclient.ui;

import com.holzclient.module.ModuleManager;
import com.holzclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HolzClientScreen extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    protected HolzClientScreen() { super(Text.of("HolzClient")); }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        int y = 20;
        int i = 0;
        for (Module m : ModuleManager.getModules()) {
            String label = (i+1) + ". " + m.getName() + " : " + (m.isEnabled() ? "ON" : "OFF");
            client.textRenderer.drawWithShadow(matrices, label, 20, y, m.isEnabled() ? 0x55FF55 : 0xFF5555);
            y += 12;
            i++;
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int y = 20;
        int i = 0;
        for (Module m : ModuleManager.getModules()) {
            if (mouseX >= 20 && mouseX <= 300 && mouseY >= y && mouseY <= y + 10) {
                m.toggle();
                return true;
            }
            y += 12; i++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() { return false; }
}
