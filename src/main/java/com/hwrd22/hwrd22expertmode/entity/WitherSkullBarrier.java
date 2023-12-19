package com.hwrd22.hwrd22expertmode.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WitherSkullBarrier extends WitherSkull {
    public WitherSkullBarrier(EntityType<? extends WitherSkull> p_37598_, Level p_37599_) {
        super(p_37598_, p_37599_);
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            if (entity != null) {
                this.moveTo(entity.getX() + 3 * Math.cos(this.tickCount % 360), entity.getY() + 1, entity.getZ() + 3 * Math.sin(this.tickCount % 360));  // orbits owner
                if (this.isInWall()) {  // block collision
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
                    this.discard();
                }
                List<Entity> entities = this.level().getEntities(this, this.getBoundingBox());
                if (!entities.isEmpty()) {  // entity collision
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
                    this.discard();
                }
            }
            else {
                if (!this.level().isClientSide) {
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
                    this.discard();
                }
            }
        }
        super.tick();
    }

    public void onHit(@NotNull HitResult result) {
        super.onHit(result);
    }
}
