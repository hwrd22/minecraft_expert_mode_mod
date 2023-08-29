package com.hwrd22.hwrd22expertmode.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VexSwordItem extends SwordItem {
    public VexSwordItem(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        performSpellCasting(p_41433_);

        p_41433_.awardStat(Stats.ITEM_USED.get(this));
        p_41433_.getCooldowns().addCooldown(this, 200);
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    protected void performSpellCasting(LivingEntity user) {
        Vec3 userLookingAt = user.getLookAngle();
        double d0 = Math.min(user.getY() + 1.25 * userLookingAt.y, user.getY());
        double d1 = Math.max(user.getY() + 1.25 * userLookingAt.y, user.getY()) + 1.0D;
        float f = (float)Mth.atan2(user.getZ() + 1.25 * userLookingAt.z - user.getZ(), (user.getX() + 1.25 * userLookingAt.x) - user.getX());  // determines direction
        for(int i = 0; i < 5; ++i) {  // inner circle
            float f1 = f + (float)i * (float)Math.PI * 0.4F;
            this.createSpellEntity(user, user.getX() + (double)Mth.cos(f1) * 1.5D, user.getZ() + (double)Mth.sin(f1) * 1.5D, d0, d1, f1, 0);
        }
        for(int k = 0; k < 8; ++k) {  // outer circle
            float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
            this.createSpellEntity(user, user.getX() + (double)Mth.cos(f2) * 2.5D, user.getZ() + (double)Mth.sin(f2) * 2.5D, d0, d1, f2, 3);
        }
        for(int l = 0; l < 16; ++l) {  // straight line
            double d2 = 1.25D * (double)(l + 1);
            this.createSpellEntity(user, user.getX() + (double)Mth.cos(f) * d2, user.getZ() + (double)Mth.sin(f) * d2, d0, d1, f, l);
        }
    }

    private void createSpellEntity(LivingEntity user, double p_32673_, double p_32674_, double p_32675_, double p_32676_, float p_32677_, int p_32678_) {
        BlockPos blockpos = BlockPos.containing(p_32673_, p_32676_, p_32674_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = user.getLevel().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(user.getLevel(), blockpos1, Direction.UP)) {
                if (!user.getLevel().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = user.getLevel().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(user.getLevel(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(p_32675_) - 1);

        if (flag) {
            user.getLevel().addFreshEntity(new EvokerFangs(user.getLevel(), p_32673_, (double)blockpos.getY() + d0, p_32674_, p_32677_, p_32678_, user));
        }

    }

    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Right-click to summon a circle of Evoker Fangs around you and a line of Evoker Fangs in front of you.").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
