package com.hwrd22.hwrd22expertmode.effect;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ExpertMode.MODID);

    public static final RegistryObject<MobEffect> FROSTBURN = MOB_EFFECTS.register("frostburn", () -> new FrostburnEffect());
    public static final RegistryObject<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding", () -> new BleedingEffect());
    public static final RegistryObject<UniversalMagicDamage> MAGIC = MOB_EFFECTS.register("magic", () -> new UniversalMagicDamage());
    public static final RegistryObject<GappleSicknessEffect> GAPPLE_SICKNESS = MOB_EFFECTS.register("gapple_sickness", () -> new GappleSicknessEffect());

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
