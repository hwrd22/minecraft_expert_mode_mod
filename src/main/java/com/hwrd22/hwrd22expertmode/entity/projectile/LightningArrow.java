package com.hwrd22.hwrd22expertmode.entity.projectile;

import com.hwrd22.hwrd22expertmode.entity.ArrowBolt;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class LightningArrow extends Arrow {
    public LightningArrow(EntityType<? extends Arrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    public LightningArrow(Level p_36861_, double p_36862_, double p_36863_, double p_36864_) {
        super(p_36861_, p_36862_, p_36863_, p_36864_);
    }

    public LightningArrow(Level p_36866_, LivingEntity p_36867_) {
        super(p_36866_, p_36867_);
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            if (entity1 instanceof LivingEntity) {
                ArrowBolt bolt = ModEntityType.LIGHTNING_BOLT_ARROW.get().create(this.level);
                bolt.setPos(this.getX(), this.getY(), this.getZ());
                bolt.addTag("from_copper");
                this.level.addFreshEntity(bolt);
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, entity)) {
                BlockPos blockpos = result.getBlockPos().relative(result.getDirection());
                ArrowBolt bolt = ModEntityType.LIGHTNING_BOLT_ARROW.get().create(this.level);
                bolt.setPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
                this.level.addFreshEntity(bolt);
            }
        }
    }
}
