package com.hwrd22.hwrd22expertmode.client;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.client.renderer.ThrownSpearRenderer;
import com.hwrd22.hwrd22expertmode.client.renderer.models.ThrownSpearModel;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import com.hwrd22.hwrd22expertmode.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ExpertMode.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void renderSetup(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ThrownSpearModel.LAYER_LOCATION, ThrownSpearModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityType.SPEAR.get(), ThrownSpearRenderer::new);
    }

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.SPEAR.get(), new ResourceLocation(ExpertMode.MODID, "raising"), (stack, level, living, id) -> {
                return living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f;
            });
        });
    }
}
