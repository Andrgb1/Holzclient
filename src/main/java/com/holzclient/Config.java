package com.holzclient;

public class Config {
    public int chunkFinderColor = 0xFFFF0000; // default ARGB red
    public int primeChunkColor = 0xFF00FF00;
    public int storageColor = 0xFF00FFFF;
    public int xrayColor = 0xFFFFFF00;
    // Safety switch: keep false by default. Only enable on your OWN private server for testing.
    public boolean allowMultiplayer = false;
    public static Config instance = new Config();
}
