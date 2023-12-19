package com.hwrd22.hwrd22expertmode.sound;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ExpertMode.MODID);

    public static final Supplier<SoundEvent> RAGE_FILLED = registerSoundEvent("rage_filled");
    public static final Supplier<SoundEvent> RAGE_USED = registerSoundEvent("rage_used");
    public static final Supplier<SoundEvent> RAGE_EMPTIED = registerSoundEvent("rage_emptied");
    public static final Supplier<SoundEvent> ADRENALINE_FILLED = registerSoundEvent("adrenaline_filled");
    public static final Supplier<SoundEvent> ADRENALINE_USED = registerSoundEvent("adrenaline_used");
    public static final Supplier<SoundEvent> ADRENALINE_EMPTIED = registerSoundEvent("adrenaline_emptied");
    public static final Supplier<SoundEvent> SPEAR_THROW = registerSoundEvent("spear_throw");
    public static final Supplier<SoundEvent> SPEAR_HIT = registerSoundEvent("spear_hit");
    public static final Supplier<SoundEvent> SPEAR_HIT_GROUND = registerSoundEvent("spear_hit_ground");
    public static final Supplier<SoundEvent> PLAYER_TELEPORTED = registerSoundEvent("player_teleported");
    public static final Supplier<SoundEvent> ICE_CAST = registerSoundEvent("ice_cast");
    public static final Supplier<SoundEvent> LIGHTNING_CAST = registerSoundEvent("lightning_cast");
    public static final Supplier<SoundEvent> GOLD_DAMAGE_NEGATE = registerSoundEvent("gold_damage_negate");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(ExpertMode.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
