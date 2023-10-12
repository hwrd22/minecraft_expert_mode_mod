package com.hwrd22.hwrd22expertmode.adrenaline;

import net.minecraft.nbt.CompoundTag;

public class PlayerAdrenaline {
    private int adrenaline;
    private boolean adrenaline_used = false;
    private final int MIN_ADRENALINE = 0;

    public int getAdrenaline() {
        return adrenaline;
    }

    public boolean getAdrenalineUse() { return adrenaline_used; }

    public void useAdrenaline() { this.adrenaline_used = true; }

    public void resetAdrenalineUse() { this.adrenaline_used = false; }

    public void addAdrenaline(int increment) {
        int MAX_ADRENALINE = 600;
        this.adrenaline = Math.min(adrenaline + increment, MAX_ADRENALINE);
    }

    public void subtractAdrenaline(int increment) {
        this.adrenaline = Math.max(adrenaline - increment, MIN_ADRENALINE);
    }

    public void resetAdrenaline() { this.adrenaline = MIN_ADRENALINE; }

    public void copyFrom(PlayerAdrenaline source) {
        this.adrenaline = source.adrenaline;
        this.adrenaline_used = source.adrenaline_used;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("adrenaline", adrenaline);
        nbt.putBoolean("adrenaline_used", adrenaline_used);
    }

    public void loadNBTData(CompoundTag nbt) {
        adrenaline = nbt.getInt("adrenaline");
        adrenaline_used = nbt.getBoolean("adrenaline_used");
    }
}
