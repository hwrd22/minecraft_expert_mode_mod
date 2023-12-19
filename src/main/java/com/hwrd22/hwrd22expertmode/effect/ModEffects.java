package com.hwrd22.hwrd22expertmode.effect;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, ExpertMode.MODID);

    public static final Supplier<MobEffect> FROSTBURN = MOB_EFFECTS.register("frostburn", FrostburnEffect::new);
    public static final Supplier<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding", BleedingEffect::new);
    public static final Supplier<UniversalMagicDamage> MAGIC = MOB_EFFECTS.register("magic", UniversalMagicDamage::new);
    public static final Supplier<GappleSicknessEffect> GAPPLE_SICKNESS = MOB_EFFECTS.register("gapple_sickness", GappleSicknessEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
