package com.hwrd22.hwrd22expertmode.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MagicFireball extends SmallFireball {
    private int life = 0;

    public MagicFireball(EntityType<? extends SmallFireball> p_37364_, Level p_37365_) {
        super(p_37364_, p_37365_);
    }

    public MagicFireball(Level p_37375_, LivingEntity p_37376_, double p_37377_, double p_37378_, double p_37379_) {
        super(p_37375_, p_37376_, p_37377_, p_37378_, p_37379_);
    }

    public MagicFireball(Level p_37367_, double p_37368_, double p_37369_, double p_37370_, double p_37371_, double p_37372_, double p_37373_) {
        super(p_37367_, p_37368_, p_37369_, p_37370_, p_37371_, p_37372_, p_37373_);
    }

    public void tick() {
        ++this.life;
        if (this.life >= 1200) {  // despawns the fireball after one minute, preventing lag
            this.discard();
        }

        Entity entity = this.getOwner();
        if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();
        } else {
            this.discard();
        }
    }
}
