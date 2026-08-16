# HolzClient

Fabric Mod (client-side) für Minecraft 1.21.11.

Build:
1. Java 17 installiert haben.
2. Im Projektordner:
   - Linux/macOS: ./gradlew build
   - Windows: .\gradlew.bat build
3. Die fertige JAR liegt in build/libs/holzclient-1.0.0.jar → in .minecraft/mods/ kopieren.

Singleplayer-Schutz:
Die Mod prüft bei jedem Client‑Tick, ob client.getServer() != null && !client.getServer().isDedicated() (siehe src/main/java/com/holzclient/module/ModuleManager.java). Wenn kein Singleplayer erkannt wird, werden aktive Module automatisch deaktiviert.
