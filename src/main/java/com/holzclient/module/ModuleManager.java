package com.holzclient.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.holzclient.Config;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();
    public static StorageESP storageESP;
    public static XRayModule xray;
    public static PlayerESP playerEsp;
    public static ChunkFinder chunkFinder;
    public static PrimeChunkFinder primeChunkFinder;
    public static BlockNotifier blockNotifier;
    public static AncientDebrisFinder debrisFinder;
    public static AutoHomeModule autoHome;

    public static void init() {
        storageESP = new StorageESP();
        xray = new XRayModule();
        playerEsp = new PlayerESP();
        chunkFinder = new ChunkFinder();
        primeChunkFinder = new PrimeChunkFinder();
        blockNotifier = new BlockNotifier();
        debrisFinder = new AncientDebrisFinder();
        autoHome = new AutoHomeModule();

        modules.add(storageESP);
        modules.add(xray);
        modules.add(playerEsp);
        modules.add(chunkFinder);
        modules.add(primeChunkFinder);
        modules.add(blockNotifier);
        modules.add(debrisFinder);
        modules.add(autoHome);
    }

    public static List<Module> getModules() { return modules; }

    // Hier wird bei jedem Tick geprüft, ob wir in Singleplayer sind.
    // Wenn nicht, werden aktivierte Module deaktiviert.
    public static void tickAll(MinecraftClient client) {
        boolean singleplayer = client.getServer() != null && !client.getServer().isDedicated();
        for (Module m : modules) {
            if (m.isEnabled() && !singleplayer) {
                m.setEnabled(false);
                if (client.player != null) {
                    client.player.sendMessage(net.minecraft.text.Text.of(m.getName() + " deaktiviert (kein Singleplayer)."), false);
                }
            }
            if (m.isEnabled()) m.tick(client);
        }
    }

    public static void renderAll(MinecraftClient client, float tickDelta) {
        for (Module m : modules) if (m.isEnabled()) m.onWorldRender(client, tickDelta);
    }

    public static void loadConfig() {
        try {
            File cfg = new File("config/holzclient.json");
            Gson g = new GsonBuilder().setPrettyPrinting().create();
            if (cfg.exists()) {
                Config.instance = g.fromJson(new FileReader(cfg), Config.class);
            } else {
                Config.instance = new Config();
                cfg.getParentFile().mkdirs();
                g.toJson(Config.instance, new FileWriter(cfg));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void saveConfig() {
        try {
            File cfg = new File("config/holzclient.json");
            Gson g = new GsonBuilder().setPrettyPrinting().create();
            g.toJson(Config.instance, new FileWriter(cfg));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
