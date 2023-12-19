package com.hwrd22.hwrd22expertmode.item;

import com.hwrd22.hwrd22expertmode.entity.projectile.FlameArrow;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlazeBowItem extends BowItem {
    public BlazeBowItem(Properties p_40660_) {
        super(p_40660_);
    }

    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        if (p_40669_ instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, p_40667_) > 0;
            ItemStack itemstack = player.getProjectile(p_40667_);

            int i = this.getUseDuration(p_40667_) - p_40670_;
            i = net.neoforged.neoforge.event.EventHooks.onArrowLoose(p_40667_, p_40668_, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, p_40667_, player));
                    if (!p_40668_.isClientSide) {
                        FlameArrow flameArrow = new FlameArrow(p_40668_, p_40669_);
                        flameArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                        flameArrow.setSecondsOnFire(100);
                        if (f == 1.0F) {
                            flameArrow.setCritArrow(true);
                        }

                        int j = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, p_40667_);
                        if (j > 0) {
                            flameArrow.setBaseDamage(flameArrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, p_40667_);
                        if (k > 0) {
                            flameArrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, p_40667_) > 0) {
                            flameArrow.setArrowBaseDamage();
                        }

                        p_40667_.hurtAndBreak(1, player, (p_276007_) -> p_276007_.broadcastBreakEvent(player.getUsedItemHand()));
                        if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                            flameArrow.pickup = FlameArrow.Pickup.CREATIVE_ONLY;
                        }

                        p_40668_.addFreshEntity(flameArrow);
                    }

                    p_40668_.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (p_40668_.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Lights all arrows on fire.\nThe Flame enchantment will increase arrow damage, and stacks with the Power enchantment.").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
