package com.hwrd22.hwrd22expertmode.client;

public class ClientRageData {
    private static int playerRage;

    public static void set(int rage) {
        ClientRageData.playerRage = rage;
    }
    public static int get() {
        return playerRage;
    }
}
