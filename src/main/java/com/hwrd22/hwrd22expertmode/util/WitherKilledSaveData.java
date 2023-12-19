package com.hwrd22.hwrd22expertmode.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public class WitherKilledSaveData extends SavedData {

    private boolean witherKilled;

    public boolean get() {
        return this.witherKilled;
    }

    public void set(boolean witherKilled) {
        this.witherKilled = witherKilled;
        this.setDirty();
    }

    public static WitherKilledSaveData create() {
        return new WitherKilledSaveData();
    }

    public static WitherKilledSaveData load(CompoundTag nbt) {
        WitherKilledSaveData data = create();
        data.witherKilled = nbt.getBoolean("witherKilled");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean("witherKilled", witherKilled);
        return nbt;
    }

    public static WitherKilledSaveData manage(MinecraftServer server) {
        Factory<WitherKilledSaveData> witherKilledSaveDataFactory = new Factory<>(WitherKilledSaveData::create, WitherKilledSaveData::load);
        return server.overworld().getDataStorage().computeIfAbsent(witherKilledSaveDataFactory, "witherKilled");
    }
}
