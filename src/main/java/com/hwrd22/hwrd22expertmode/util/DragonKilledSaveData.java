package com.hwrd22.hwrd22expertmode.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class DragonKilledSaveData extends SavedData {

    private boolean dragonKilled;

    public boolean get() {
        return this.dragonKilled;
    }

    public void set(boolean dragonKilled) {
        this.dragonKilled = dragonKilled;
        this.setDirty();
    }

    public static DragonKilledSaveData create() {
        return new DragonKilledSaveData();
    }

    public static DragonKilledSaveData load(CompoundTag nbt) {
        DragonKilledSaveData data = create();
        data.dragonKilled = nbt.getBoolean("dragonKilled");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean("dragonKilled", dragonKilled);
        return nbt;
    }

    public static DragonKilledSaveData manage(MinecraftServer server) {
        Factory<DragonKilledSaveData> dragonKilledSaveDataFactory = new Factory<>(DragonKilledSaveData::create, DragonKilledSaveData::load);
        return server.overworld().getDataStorage().computeIfAbsent(dragonKilledSaveDataFactory, "dragonKilled");
    }
}
