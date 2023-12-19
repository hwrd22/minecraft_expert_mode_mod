package com.hwrd22.hwrd22expertmode.adrenaline;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerAdrenalineProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerAdrenaline> PLAYER_ADRENALINE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerAdrenaline adrenaline = null;
    private final LazyOptional<PlayerAdrenaline> optional = LazyOptional.of(this::createPlayerAdrenaline);

    private PlayerAdrenaline createPlayerAdrenaline() {
        if (this.adrenaline == null) {
            this.adrenaline = new PlayerAdrenaline();
        }

        return this.adrenaline;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_ADRENALINE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerAdrenaline().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerAdrenaline().loadNBTData(nbt);
    }
}
