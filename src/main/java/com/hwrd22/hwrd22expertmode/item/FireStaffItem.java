package com.hwrd22.hwrd22expertmode.item;

import com.hwrd22.hwrd22expertmode.entity.projectile.MagicFireball;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;

public class FireStaffItem extends ProjectileWeaponItem implements Vanishable {
    public FireStaffItem(Properties p_43009_) {
        super(p_43009_);
    }

    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        int i = this.getUseDuration(p_40667_) - p_40670_;
        if (i < 0) return;

        float f = getPowerForTime(i);
        if(!((double)f < 0.10)) {
            if (!p_40668_.isClientSide) {
                Vec3 vec3d = p_40669_.getLookAngle();
                double d2 = p_40669_.getX() - (p_40669_.getX() + vec3d.x * 4.0D);
                double d3 = (p_40669_.getEyeY() - (p_40669_.getEyeY() + vec3d.y * 4.0d)) + 1;
                double d4 = p_40669_.getZ() - (p_40669_.getZ() + vec3d.z * 4.0D);
                MagicFireball fireball = new MagicFireball(p_40668_, p_40669_, -d2, -d3, -d4);
                fireball.setPos(p_40669_.getEyePosition());
                fireball.shootFromRotation(p_40669_, p_40669_.getXRot(), p_40669_.getYRot(), 0.0f, f * 0.5F, 0.0f);
                p_40667_.hurtAndBreak(1, p_40669_, (p_276007_) -> p_276007_.broadcastBreakEvent(p_40669_.getUsedItemHand()));
                p_40668_.addFreshEntity(fireball);
            }
            if (p_40669_ instanceof Player player) {
                player.getCooldowns().addCooldown(this, 20);
                player.getCooldowns().addCooldown(ModItems.ICE_STAFF.get(), 20);
                player.getCooldowns().addCooldown(ModItems.LIGHTNING_STAFF.get(), 40);
            }
            p_40668_.playSound((Player)null, p_40669_.getX(), p_40669_.getY(), p_40669_.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        if (p_40669_ instanceof Player player)
            player.awardStat(Stats.ITEM_USED.get(this));
    }

    public static float getPowerForTime(int p_40662_) {
        float f = (float)p_40662_ / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, p_40672_, p_40673_, p_40674_, true);
        if (ret != null) return ret;
        p_40673_.startUsingItem(p_40674_);
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return null;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Fires a small Fireball. The Fireball's power is constant.").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
