package com.hwrd22.hwrd22expertmode.client.renderer;

import com.hwrd22.hwrd22expertmode.entity.projectile.WitherArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class WitherArrowRenderer<T extends WitherArrow> extends ArrowRenderer {
    public WitherArrowRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity p_114482_) {
        return new ResourceLocation("minecraft:textures/entity/projectiles/arrow.png");
    }
}
