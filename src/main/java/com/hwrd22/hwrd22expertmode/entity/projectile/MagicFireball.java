package com.hwrd22.hwrd22expertmode.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;

public class MagicFireball extends SmallFireball {
    private int life = 0;

    public MagicFireball(Level p_37375_, LivingEntity p_37376_, double p_37377_, double p_37378_, double p_37379_) {
        super(p_37375_, p_37376_, p_37377_, p_37378_, p_37379_);
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
