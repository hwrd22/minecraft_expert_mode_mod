package com.hwrd22.hwrd22expertmode.sound;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExpertMode.MODID);

    public static final RegistryObject<SoundEvent> RAGE_FILLED = registerSoundEvent("rage_filled");
    public static final RegistryObject<SoundEvent> RAGE_USED = registerSoundEvent("rage_used");
    public static final RegistryObject<SoundEvent> RAGE_EMPTIED = registerSoundEvent("rage_emptied");
    public static final RegistryObject<SoundEvent> ADRENALINE_FILLED = registerSoundEvent("adrenaline_filled");
    public static final RegistryObject<SoundEvent> ADRENALINE_USED = registerSoundEvent("adrenaline_used");
    public static final RegistryObject<SoundEvent> ADRENALINE_EMPTIED = registerSoundEvent("adrenaline_emptied");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(ExpertMode.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
