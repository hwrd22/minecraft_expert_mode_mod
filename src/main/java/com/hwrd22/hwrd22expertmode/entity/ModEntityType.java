package com.hwrd22.hwrd22expertmode.entity;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import com.hwrd22.hwrd22expertmode.entity.projectile.ThrownSpear;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntityType {
    private ModEntityType() {}
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExpertMode.MODID);
    public static final RegistryObject<EntityType<ThrownSpear>> SPEAR = ENTITIES.register("spear", () -> EntityType.Builder.of((EntityType.EntityFactory<ThrownSpear>)ThrownSpear::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(ExpertMode.MODID, "spear").toString()));

    private static <T extends Entity> EntityType<T> register(String p_20635_, EntityType.Builder<T> p_20636_) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, p_20635_, p_20636_.build(p_20635_));
    }
}
