package com.hwrd22.hwrd22expertmode.util;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class EndgameSaveData extends SavedData {

    private boolean endgameEnabled;

    public boolean get() {
        return this.endgameEnabled;
    }

    public void set(boolean endgameEnabled) {
        this.endgameEnabled = endgameEnabled;
        this.setDirty();
    }

    public static EndgameSaveData create() {
        return new EndgameSaveData();
    }

    public static EndgameSaveData load(CompoundTag nbt) {
        EndgameSaveData data = create();
        data.endgameEnabled = nbt.getBoolean("endgameEnabled");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean("endgameEnabled", endgameEnabled);
        return nbt;
    }

    public static EndgameSaveData manage(MinecraftServer server) {
        Factory<EndgameSaveData> endgameSaveDataFactory = new Factory<>(EndgameSaveData::create, EndgameSaveData::load);
        return server.overworld().getDataStorage().computeIfAbsent(endgameSaveDataFactory, "endgameEnabled");
    }
}
