package com.hwrd22.hwrd22expertmode.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;

public class FrostburnEffect extends MobEffect {
    protected FrostburnEffect() {
        super(MobEffectCategory.HARMFUL, 12121087);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int Amplifier) {
        if(!entity.level.isClientSide()) {
            if (entity instanceof Skeleton)  // ignoring skeletons and strays since they're not affected by freeze
                entity.hurt(entity.damageSources().magic(), 1.5f);
            else
                entity.hurt(entity.damageSources().freeze(), 1.5f);
        }
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int j = 25 >> p_19456_;
        if (j > 0)
            return p_19455_ % j == 0;
        else
            return true;
    }
}
