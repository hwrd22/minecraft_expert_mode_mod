package com.hwrd22.hwrd22expertmode.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class UniversalMagicDamage extends InstantenousMobEffect {
    protected UniversalMagicDamage(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        p_19467_.hurt(p_19467_.damageSources().magic(), (float)(6 << p_19468_));
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity p_19462_, @Nullable Entity p_19463_, LivingEntity p_19464_, int p_19465_, double p_19466_) {
        int j = (int) (p_19466_ * (double) (6 << p_19465_) + 0.5D);
        if (p_19462_ == null) {
            p_19464_.hurt(p_19464_.damageSources().magic(), (float) j);
        } else {
            p_19464_.hurt(p_19464_.damageSources().indirectMagic(p_19462_, p_19463_), (float) j);
        }
    }

}
