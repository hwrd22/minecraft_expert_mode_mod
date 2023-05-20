package com.hwrd22.hwrd22expertmode.client;

public class ClientAdrenalineData {
    private static int playerAdrenaline;

    public static void set(int adrenaline) {
        ClientAdrenalineData.playerAdrenaline = adrenaline;
    }
    public static int get() {
        return playerAdrenaline;
    }
}
