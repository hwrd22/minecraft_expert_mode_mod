package com.hwrd22.hwrd22expertmode.effect;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ExpertMode.MODID);

    public static final RegistryObject<MobEffect> FROSTBURN = MOB_EFFECTS.register("frostburn", () -> new FrostburnEffect(MobEffectCategory.HARMFUL, 12121087));
    public static final RegistryObject<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding", () -> new BleedingEffect(MobEffectCategory.HARMFUL, 8847360));
    public static final RegistryObject<UniversalMagicDamage> MAGIC = MOB_EFFECTS.register("magic", () -> new UniversalMagicDamage(MobEffectCategory.HARMFUL, 0));
    public static final RegistryObject<GappleSicknessEffect> GAPPLE_SICKNESS = MOB_EFFECTS.register("gapple_sickness", () -> new GappleSicknessEffect(MobEffectCategory.HARMFUL, 8228616));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
