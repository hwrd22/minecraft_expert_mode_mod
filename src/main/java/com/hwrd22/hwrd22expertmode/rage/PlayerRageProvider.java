package com.hwrd22.hwrd22expertmode.rage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerRageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerRage> PLAYER_RAGE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerRage rage = null;
    private final LazyOptional<PlayerRage> optional = LazyOptional.of(this::createPlayerRage);

    private PlayerRage createPlayerRage() {
        if (this.rage == null) {
            this.rage = new PlayerRage();
        }

        return this.rage;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_RAGE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerRage().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerRage().loadNBTData(nbt);
    }
}
