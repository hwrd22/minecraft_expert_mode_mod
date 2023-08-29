package com.hwrd22.hwrd22expertmode.entity.projectile;

import com.hwrd22.hwrd22expertmode.effect.ModEffects;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class IceBall extends AbstractHurtingProjectile {
    private int life = 0;
    public IceBall(EntityType<? extends IceBall> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public IceBall(EntityType<? extends AbstractHurtingProjectile> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        super(p_36817_, p_36818_, p_36819_, p_36820_, p_36821_, p_36822_, p_36823_, p_36824_);
    }

    public IceBall(EntityType<? extends AbstractHurtingProjectile> p_36826_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_) {
        super(p_36826_, p_36827_, p_36828_, p_36829_, p_36830_, p_36831_);
    }

    public IceBall(Level p_37375_, LivingEntity p_37376_, double p_37377_, double p_37378_, double p_37379_) {
        super(ModEntityType.ICE_BALL.get(), p_37376_, p_37377_, p_37378_, p_37379_, p_37375_);
    }

    public void tick() {
        ++this.life;
        if (this.life >= 1200) {  // despawns the iceball after one minute, preventing lag
            this.discard();
        }

        Entity entity = this.getOwner();
        if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();

            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
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
                    float f1 = 0.25F;
                    this.level.addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }

                f = 0.8F;
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double) f));
            this.level.addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    protected void onHitEntity(EntityHitResult p_37386_) {
        super.onHitEntity(p_37386_);
        if (!this.level.isClientSide) {
            Entity entity = p_37386_.getEntity();
            Entity entity1 = this.getOwner();
            if (entity1 instanceof LivingEntity) {
                if (entity instanceof LivingEntity) {
                    this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.withPropertiesOf(Blocks.REDSTONE_ORE.defaultBlockState())), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    this.level.playSound((Player) null, p_37386_.getEntity().getOnPos(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                    MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 2);
                    MobEffectInstance freezing = new MobEffectInstance(ModEffects.FROSTBURN.get(), 200, 0);
                    if (((LivingEntity) entity).hasEffect(ModEffects.FROSTBURN.get()))
                        freezing = new MobEffectInstance(ModEffects.FROSTBURN.get(), 200, 1);
                    ((LivingEntity) entity).addEffect(slow);
                    ((LivingEntity) entity).addEffect(freezing);
                }
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
        }

        this.discard();
    }

    protected void onHitBlock(BlockHitResult p_37384_) {
        super.onHitBlock(p_37384_);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, entity)) {
                BlockPos blockpos = p_37384_.getBlockPos().relative(p_37384_.getDirection());
                if (this.level.isEmptyBlock(blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, Blocks.POWDER_SNOW.defaultBlockState());
                }
            }
            this.level.playSound((Player) null, p_37384_.getBlockPos(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);

        }
        this.discard();
    }

    protected void onHit(HitResult p_37388_) {
        super.onHit(p_37388_);
        if (!this.level.isClientSide) {
            this.discard();
        }

    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource p_37381_, float p_37382_) {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SNOWFLAKE;
    }


}
