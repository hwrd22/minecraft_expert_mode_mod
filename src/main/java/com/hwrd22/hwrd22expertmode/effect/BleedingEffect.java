package com.hwrd22.hwrd22expertmode.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;

public class BleedingEffect extends MobEffect {
    protected BleedingEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int Amplifier) {
        if(!entity.level.isClientSide()) {
            entity.hurt(entity.damageSources().generic(), 1.25f);
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
