package com.hwrd22.hwrd22expertmode.item;

import com.hwrd22.hwrd22expertmode.effect.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class DamnedAxeItem extends AxeItem {
    public DamnedAxeItem(Tier p_40521_, float p_40522_, float p_40523_, Properties p_40524_) {
        super(p_40521_, p_40522_, p_40523_, p_40524_);
    }

    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack p_40994_, LivingEntity p_40995_, LivingEntity p_40996_) {
        super.hurtEnemy(p_40994_, p_40995_, p_40996_);
        if (p_40995_.hasEffect(ModEffects.BLEEDING.get()))
            p_40995_.addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(), 200, 1));
        else
            p_40995_.addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(), 200));
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, @NotNull TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Entities hit with this axe will begin to bleed.\nBleeding entities that are hit with this weapon will bleed faster.").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
