package com.hwrd22.hwrd22expertmode.entity.projectile;

import com.hwrd22.hwrd22expertmode.entity.ArrowBolt;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class LightningArrow extends Arrow {
    public LightningArrow(EntityType<? extends Arrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    public LightningArrow(Level p_36866_, LivingEntity p_36867_) {
        super(p_36866_, p_36867_);
    }

    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            if (entity1 instanceof LivingEntity) {
                ArrowBolt bolt = ModEntityType.LIGHTNING_BOLT_ARROW.get().create(this.level());
                assert bolt != null;
                bolt.setPos(this.getX(), this.getY(), this.getZ());
                bolt.addTag("from_copper");
                this.level().addFreshEntity(bolt);
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
        }
    }

    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (!this.level().isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.neoforged.neoforge.event.EventHooks.getMobGriefingEvent(this.level(), entity)) {
                ArrowBolt bolt = ModEntityType.LIGHTNING_BOLT_ARROW.get().create(this.level());
                assert bolt != null;
                bolt.setPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
                this.level().addFreshEntity(bolt);
            }
        }
    }
}
