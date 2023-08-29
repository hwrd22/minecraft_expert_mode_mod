package com.hwrd22.hwrd22expertmode.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlameArrow extends Arrow {
    private static final double ARROW_BASE_DAMAGE = 2.0D;
    private static final double ARROW_ENHANCED_DAMAGE = 3.0D;
    private static boolean FLAME_ENCHANTMENT = false;
    public FlameArrow(EntityType<? extends FlameArrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    public FlameArrow(Level p_36866_, LivingEntity p_36867_) {
        super(p_36866_, p_36867_);
    }

    public void tick() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.level.addParticle(ParticleTypes.FLAME, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);  // why the fuck isn't the particle spawning
        super.tick();
    }

    public double getBaseDamage() {
        if (FLAME_ENCHANTMENT)
            return ARROW_ENHANCED_DAMAGE;
        else
            return ARROW_BASE_DAMAGE;
    }

    public void setArrowBaseDamage() {
        FLAME_ENCHANTMENT = true;
    }
}
