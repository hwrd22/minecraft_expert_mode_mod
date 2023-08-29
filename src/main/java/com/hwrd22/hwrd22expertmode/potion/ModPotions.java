package com.hwrd22.hwrd22expertmode.potion;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ExpertMode.MODID);

    public static final RegistryObject<Potion> FROSTBURN = POTIONS.register("frostburn", () -> new Potion(new MobEffectInstance(ModEffects.FROSTBURN.get(), 2000, 0), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2000, 0)));
    public static final RegistryObject<Potion> HASTE = POTIONS.register("haste", () -> new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 0)));
    public static final RegistryObject<Potion> STRONG_HASTE = POTIONS.register("strong_haste", () -> new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 1)));
    public static final RegistryObject<Potion> SPIDER_FLASK = POTIONS.register("spider_flask", () -> new Potion(new MobEffectInstance(MobEffects.NIGHT_VISION, 1800), new MobEffectInstance(MobEffects.POISON, 100)));
    public static final RegistryObject<Potion> ROTTEN = POTIONS.register("rotten", () -> new Potion(new MobEffectInstance(MobEffects.CONFUSION, 2000), new MobEffectInstance(MobEffects.POISON, 2000, 3)));
    public static final RegistryObject<Potion> CURSED = POTIONS.register("cursed", () -> new Potion(new MobEffectInstance(MobEffects.CONFUSION, 2000), new MobEffectInstance(MobEffects.WITHER, 2000, 2)));
    public static final RegistryObject<Potion> WITHER = POTIONS.register("wither", () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 2000)));

    public static void register(IEventBus bus) {
        POTIONS.register(bus);
    }
}
