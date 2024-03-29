package com.hwrd22.hwrd22expertmode.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IMobEffectExtension;

import java.util.ArrayList;
import java.util.List;

public class GappleSicknessEffect extends MobEffect implements IMobEffectExtension{
    protected GappleSicknessEffect() {
        super(MobEffectCategory.HARMFUL, 8228616);
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int j = 25 >> p_19456_;
        if (j > 0)
            return p_19455_ % j == 0;
        else
            return true;
    }

    @Override
    public List<ItemStack> getCurativeItems() {  // Modified so milk cannot clear this effect
        return new ArrayList<>();
    }


}
