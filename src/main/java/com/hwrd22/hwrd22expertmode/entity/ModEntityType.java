package com.hwrd22.hwrd22expertmode.entity;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.entity.projectile.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.Supplier;

public final class ModEntityType {
    private ModEntityType() {}
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ExpertMode.MODID);
    public static final Supplier<EntityType<ThrownSpear>> SPEAR = ENTITIES.register("spear", () -> EntityType.Builder.of((EntityType.EntityFactory<ThrownSpear>)ThrownSpear::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(ExpertMode.MODID, "spear").toString()));
    public static final Supplier<EntityType<ThrownGildedTrident>> GILDED_TRIDENT = ENTITIES.register("gilded_trident", () -> EntityType.Builder.of((EntityType.EntityFactory<ThrownGildedTrident>)ThrownGildedTrident::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(ExpertMode.MODID, "gilded_trident").toString()));
    public static final Supplier<EntityType<ThrownFakeGildedTrident>> FAKE_GILDED_TRIDENT = ENTITIES.register("fake_gilded_trident", () -> EntityType.Builder.of((EntityType.EntityFactory<ThrownFakeGildedTrident>)ThrownFakeGildedTrident::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(ExpertMode.MODID, "fake_gilded_trident").toString()));
    public static final Supplier<EntityType<ThrownPoseidonTrident>> POSEIDON_TRIDENT = ENTITIES.register("poseidon_trident", () -> EntityType.Builder.of((EntityType.EntityFactory<ThrownPoseidonTrident>)ThrownPoseidonTrident::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(ExpertMode.MODID, "poseidon_trident").toString()));
    public static final Supplier<EntityType<IceBall>> ICE_BALL = ENTITIES.register("ice_ball", () -> EntityType.Builder.of((EntityType.EntityFactory<IceBall>)IceBall::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new ResourceLocation(ExpertMode.MODID, "ice_ball").toString()));
    public static final Supplier<EntityType<LightningBall>> LIGHTNING_BALL = ENTITIES.register("lightning_ball", () -> EntityType.Builder.of((EntityType.EntityFactory<LightningBall>)LightningBall::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new ResourceLocation(ExpertMode.MODID, "lightning_ball").toString()));
    public static final Supplier<EntityType<LightningArrow>> LIGHTNING_ARROW = ENTITIES.register("lightning_arrow", () -> EntityType.Builder.of((EntityType.EntityFactory<LightningArrow>)LightningArrow::new, MobCategory.MISC).build(new ResourceLocation(ExpertMode.MODID, "lightning_arrow").toString()));
    public static final Supplier<EntityType<FlameArrow>> FLAME_ARROW = ENTITIES.register("flame_arrow", () -> EntityType.Builder.of((EntityType.EntityFactory<FlameArrow>)FlameArrow::new, MobCategory.MISC).build(new ResourceLocation(ExpertMode.MODID, "flame_arrow").toString()));
    public static final Supplier<EntityType<DragonArrow>> DRAGON_ARROW = ENTITIES.register("dragon_arrow", () -> EntityType.Builder.of((EntityType.EntityFactory<DragonArrow>)DragonArrow::new, MobCategory.MISC).build(new ResourceLocation(ExpertMode.MODID, "dragon_arrow").toString()));
    public static final Supplier<EntityType<WitherArrow>> WITHER_ARROW = ENTITIES.register("wither_arrow", () -> EntityType.Builder.of((EntityType.EntityFactory<WitherArrow>)WitherArrow::new, MobCategory.MISC).build(new ResourceLocation(ExpertMode.MODID, "wither_arrow").toString()));
    public static final Supplier<EntityType<ArrowBolt>> LIGHTNING_BOLT_ARROW = ENTITIES.register("lightning_bolt_arrow", () -> EntityType.Builder.of((EntityType.EntityFactory<ArrowBolt>)ArrowBolt::new, MobCategory.MISC).build(new ResourceLocation(ExpertMode.MODID, "lightning_bolt_arrow").toString()));
    public static final Supplier<EntityType<WitherSkullBarrier>> SKULL_BARRIER = ENTITIES.register("skull_barrier", () -> EntityType.Builder.of((EntityType.EntityFactory<WitherSkullBarrier>)WitherSkullBarrier::new, MobCategory.MISC).build(new ResourceLocation(ExpertMode.MODID, "skull_barrier").toString()));

    private static <T extends Entity> EntityType<T> register(String p_20635_, EntityType.Builder<T> p_20636_) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, p_20635_, p_20636_.build(p_20635_));
    }
}
