package com.hwrd22.hwrd22expertmode.client;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.client.renderer.*;
import com.hwrd22.hwrd22expertmode.client.renderer.models.ThrownFakeGildedTridentModel;
import com.hwrd22.hwrd22expertmode.client.renderer.models.ThrownGildedTridentModel;
import com.hwrd22.hwrd22expertmode.client.renderer.models.ThrownPoseidonTridentModel;
import com.hwrd22.hwrd22expertmode.client.renderer.models.ThrownSpearModel;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import com.hwrd22.hwrd22expertmode.item.JitterShotbowItem;
import com.hwrd22.hwrd22expertmode.item.ModItems;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ExpertMode.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void renderSetup(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ThrownSpearModel.LAYER_LOCATION, ThrownSpearModel::createBodyLayer);
        event.registerLayerDefinition(ThrownGildedTridentModel.LAYER_LOCATION, ThrownGildedTridentModel::createBodyLayer);
        event.registerLayerDefinition(ThrownFakeGildedTridentModel.LAYER_LOCATION, ThrownFakeGildedTridentModel::createBodyLayer);
        event.registerLayerDefinition(ThrownPoseidonTridentModel.LAYER_LOCATION, ThrownPoseidonTridentModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityType.SPEAR.get(), ThrownSpearRenderer::new);
        event.registerEntityRenderer(ModEntityType.GILDED_TRIDENT.get(), ThrownGildedTridentRenderer::new);
        event.registerEntityRenderer(ModEntityType.FAKE_GILDED_TRIDENT.get(), ThrownFakeGildedTridentRenderer::new);
        event.registerEntityRenderer(ModEntityType.POSEIDON_TRIDENT.get(), ThrownPoseidonTridentRenderer::new);
        event.registerEntityRenderer(ModEntityType.ICE_BALL.get(), IceBallRenderer::new);
        event.registerEntityRenderer(ModEntityType.LIGHTNING_BALL.get(), LightningBallRenderer::new);
        event.registerEntityRenderer(ModEntityType.LIGHTNING_ARROW.get(), LightningArrowRenderer::new);
        event.registerEntityRenderer(ModEntityType.FLAME_ARROW.get(), FlameArrowRenderer::new);
        event.registerEntityRenderer(ModEntityType.DRAGON_ARROW.get(), DragonArrowRenderer::new);
        event.registerEntityRenderer(ModEntityType.WITHER_ARROW.get(), WitherArrowRenderer::new);
        event.registerEntityRenderer(ModEntityType.LIGHTNING_BOLT_ARROW.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(ModEntityType.SKULL_BARRIER.get(), SkullBarrierRenderer::new);
    }

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.SPEAR.get(), new ResourceLocation(ExpertMode.MODID, "raising"), (stack, level, living, id) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
            ItemProperties.register(ModItems.GILDED_TRIDENT.get(), new ResourceLocation(ExpertMode.MODID, "raising"), (stack, level, living, id) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
            ItemProperties.register(ModItems.BLAZE_BOW.get(), new ResourceLocation(ExpertMode.MODID, "pulling"), (stack, level, living, id) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
            ItemProperties.register(ModItems.JITTER_SHOTBOW.get(), new ResourceLocation(ExpertMode.MODID, "pulling"), (stack, level, living, id) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
            ItemProperties.register(ModItems.JITTER_SHOTBOW.get(), new ResourceLocation(ExpertMode.MODID, "charged"), (stack, level, living, id) -> living != null && JitterShotbowItem.isCharged(stack) ? 1.0f : 0.0f);
            ItemProperties.register(ModItems.JITTER_SHOTBOW.get(), new ResourceLocation(ExpertMode.MODID, "firework"), (stack, level, living, id) -> living != null && JitterShotbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0f : 0.0f);
            ItemProperties.register(ModItems.BLAZE_BOW.get(), new ResourceLocation(ExpertMode.MODID, "pull"), (stack, level, living, id) -> {
                if (living != null && living.isUsingItem()) {
                    if (living.getTicksUsingItem() >= 22)
                        return 1.0f;
                    else
                        return (float) living.getTicksUsingItem() / 22;
                }
                else
                    return 0f;
            });
        });
    }
}
