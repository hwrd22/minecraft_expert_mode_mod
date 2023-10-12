package com.hwrd22.hwrd22expertmode.rage;

import net.minecraft.nbt.CompoundTag;

public class PlayerRage {
    private int rage;
    private boolean rage_used = false;

    public int getRage() {
        return rage;
    }

    public boolean getRageUse() { return rage_used; }

    public void useRage() { this.rage_used = true; }

    public void resetRageUse() { this.rage_used = false; }

    public void addRage(int increment) {
        int MAX_RAGE = 10000;
        this.rage = Math.min(rage + increment, MAX_RAGE);
    }

    public void subtractRage(int increment) {
        int MIN_RAGE = 0;
        this.rage = Math.max(rage - increment, MIN_RAGE);
    }

    public void copyFrom(PlayerRage source) {
        this.rage = source.rage;
        this.rage_used = source.rage_used;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("rage", rage);
        nbt.putBoolean("rage_used", rage_used);
    }

    public void loadNBTData(CompoundTag nbt) {
        rage = nbt.getInt("rage");
        rage_used = nbt.getBoolean("rage_used");
    }
}
