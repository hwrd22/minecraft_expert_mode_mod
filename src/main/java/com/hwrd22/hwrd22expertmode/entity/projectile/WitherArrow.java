package com.hwrd22.hwrd22expertmode.entity.projectile;

import com.google.common.collect.Sets;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class WitherArrow extends AbstractArrow {
    private static final double ARROW_BASE_DAMAGE = 4.0D;
    private final Set<MobEffectInstance> effects = Sets.newHashSet();
    private final Potion potion = Potions.EMPTY;
    public WitherArrow(EntityType<? extends WitherArrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    public WitherArrow(Level p_36866_, LivingEntity p_36867_) {
        super(ModEntityType.WITHER_ARROW.get(), p_36867_, p_36866_);
    }

    public void tick() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.level().addParticle(ParticleTypes.SMOKE, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);  // why the fuck isn't the particle spawning
        super.tick();
    }

    protected void onHitEntity(@NotNull EntityHitResult p_37626_) {
        super.onHitEntity(p_37626_);
        if (!this.level().isClientSide) {
            Entity entity = p_37626_.getEntity();
            Entity entity1 = this.getOwner();
            boolean flag;
            if (entity1 instanceof LivingEntity livingentity) {
                flag = entity.hurt(this.damageSources().explosion(this, livingentity), 8.0F);
                if (flag) {
                    if (entity.isAlive()) {
                        this.doEnchantDamageEffects(livingentity, entity);
                    } else {
                        livingentity.heal(5.0F);
                    }
                }
            } else {
                flag = entity.hurt(this.damageSources().magic(), 5.0F);
            }

            if (flag && entity instanceof LivingEntity livingentity1) {
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 40;
                }

                if (i > 0) {
                    livingentity1.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * i, 1), this.getEffectSource());
                }
            }

        }
    }

    @Override
    protected ItemStack getPickupItem() {
        if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
            return new ItemStack(Items.ARROW);
        } else {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.setPotion(itemstack, this.potion);
            PotionUtils.setCustomEffects(itemstack, this.effects);

            return itemstack;
        }
    }

    protected void onHit(@NotNull HitResult p_36913_) {
        super.onHit(p_36913_);
        if (p_36913_.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult)p_36913_).getEntity())) {
            if (!this.level().isClientSide) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
                AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity) {
                    areaeffectcloud.setOwner((LivingEntity)entity);
                }

                areaeffectcloud.setParticle(ParticleTypes.SMOKE);
                areaeffectcloud.setRadius(3.0F);
                areaeffectcloud.setDuration(600);
                areaeffectcloud.setRadiusPerTick((7.0F - areaeffectcloud.getRadius()) / (float)areaeffectcloud.getDuration());
                areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1));
                if (!list.isEmpty()) {
                    for(LivingEntity livingentity : list) {
                        double d0 = this.distanceToSqr(livingentity);
                        if (d0 < 16.0D) {
                            areaeffectcloud.setPos(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                            break;
                        }
                    }
                }

                this.level().addFreshEntity(areaeffectcloud);
                this.discard();
            }
        }
    }

    public double getBaseDamage() {
        return ARROW_BASE_DAMAGE;
    }
}
