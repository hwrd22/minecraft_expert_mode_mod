package com.hwrd22.hwrd22expertmode.entity.projectile;

import com.hwrd22.hwrd22expertmode.entity.ArrowBolt;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LightningBall extends AbstractHurtingProjectile {
    private int life = 0;
    public LightningBall(EntityType<? extends LightningBall> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public LightningBall(EntityType<? extends AbstractHurtingProjectile> p_36826_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_) {
        super(p_36826_, p_36827_, p_36828_, p_36829_, p_36830_, p_36831_);
    }

    public LightningBall(Level p_37375_, LivingEntity p_37376_, double p_37377_, double p_37378_, double p_37379_) {
        super(ModEntityType.LIGHTNING_BALL.get(), p_37376_, p_37377_, p_37378_, p_37379_, p_37375_);
    }

    public void tick() {
        ++this.life;
        if (this.life >= 1200) {  // despawns the lightningball after one minute, preventing lag
            this.discard();
        }

        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().isLoaded(this.blockPosition())) {
            super.tick();

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }

                f = 0.8F;
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale(f));
            this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    protected void onHitEntity(@NotNull EntityHitResult p_37386_) {
        super.onHitEntity(p_37386_);
        if (!this.level().isClientSide) {
            Entity entity = p_37386_.getEntity();
            Entity entity1 = this.getOwner();
            if (entity1 instanceof LivingEntity) {
                ArrowBolt bolt = ModEntityType.LIGHTNING_BOLT_ARROW.get().create(this.level());
                assert bolt != null;
                bolt.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(bolt);
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
        }
        this.discard();
    }

    protected void onHitBlock(@NotNull BlockHitResult p_37384_) {
        super.onHitBlock(p_37384_);
        if (!this.level().isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.neoforged.neoforge.event.EventHooks.getMobGriefingEvent(this.level(), entity)) {
                ArrowBolt bolt = ModEntityType.LIGHTNING_BOLT_ARROW.get().create(this.level());
                assert bolt != null;
                bolt.setPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
                this.level().addFreshEntity(bolt);
            }
        }
        this.discard();
    }

    protected void onHit(@NotNull HitResult p_37388_) {
        super.onHit(p_37388_);
        if (!this.level().isClientSide) {
            this.discard();
        }

    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(@NotNull DamageSource p_37381_, float p_37382_) {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.ELECTRIC_SPARK;
    }


}
