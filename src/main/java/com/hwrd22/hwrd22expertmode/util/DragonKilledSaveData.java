package com.hwrd22.hwrd22expertmode.util;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

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
    @MethodsReturnNonnullByDefault
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean("dragonKilled", dragonKilled);
        return nbt;
    }

    public static DragonKilledSaveData manage(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(DragonKilledSaveData::load, DragonKilledSaveData::create, "dragonKilled");
    }
}
