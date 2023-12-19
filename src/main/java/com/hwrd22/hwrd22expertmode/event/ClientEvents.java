package com.hwrd22.hwrd22expertmode.event;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.client.AdrenalineHudOverlay;
import com.hwrd22.hwrd22expertmode.client.RageHudOverlay;
import com.hwrd22.hwrd22expertmode.client.renderer.models.EnderDragonHeadModel;
import com.hwrd22.hwrd22expertmode.item.ModItems;
import com.hwrd22.hwrd22expertmode.networking.ModMessages;
import com.hwrd22.hwrd22expertmode.networking.packet.AdrenalineUseC2SPacket;
import com.hwrd22.hwrd22expertmode.networking.packet.RageUseC2SPacket;
import com.hwrd22.hwrd22expertmode.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ExpertMode.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.RAGE_KEY);
            event.register(KeyBinding.ADRENALINE_KEY);
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.RAGE_KEY.consumeClick()) {
                // Add Rage logic here and replace below line
                ModMessages.sendToServer(new RageUseC2SPacket());
            }
            if (KeyBinding.ADRENALINE_KEY.consumeClick()) {
                ModMessages.sendToServer(new AdrenalineUseC2SPacket());
            }
        }

        @SubscribeEvent
        public static void fovModifier(ComputeFovModifierEvent event) {
            float f = 1.0f;
            ItemStack itemstack = event.getPlayer().getUseItem();
            if (event.getPlayer().isUsingItem()) {
                if (itemstack.is(ModItems.DRAGON_BOW.get()) || itemstack.is(ModItems.WITHER_BOW.get()) || itemstack.is(ModItems.BLAZE_BOW.get()) || itemstack.is(ModItems.FIRE_STAFF.get()) || itemstack.is(ModItems.ICE_STAFF.get()) || itemstack.is(ModItems.LIGHTNING_STAFF.get())) {
                    int i = event.getPlayer().getTicksUsingItem();
                    float f1 = (float) i / 20.0F;
                    if (f1 > 1.0F) {
                        f1 = 1.0F;
                    } else {
                        f1 *= f1;
                    }

                    f *= 1.0F - f1 * 0.15F;
                } else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && event.getPlayer().isScoping()) {
                    f = 0.1f;
                }
            }
            if (event.getPlayer().getAbilities().flying) {
                f *= 1.1F;
            }

            f *= ((float)event.getPlayer().getAttributeValue(Attributes.MOVEMENT_SPEED) / event.getPlayer().getAbilities().getWalkingSpeed() + 1.0F) / 2.0F;
            if (event.getPlayer().getAbilities().getWalkingSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
                f = 1.0F;
            }

            if (event.getPlayer().isUsingItem()) {
                if (itemstack.is(Items.BOW)) {
                    int i = event.getPlayer().getTicksUsingItem();
                    float f1 = (float)i / 20.0F;
                    if (f1 > 1.0F) {
                        f1 = 1.0F;
                    } else {
                        f1 *= f1;
                    }

                    f *= 1.0F - f1 * 0.15F;
                } else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && event.getPlayer().isScoping()) {
                    f = 0.1f;
                }
            }

            event.setNewFovModifier(f);
        }
    }
    @Mod.EventBusSubscriber(modid = ExpertMode.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.RAGE_KEY);
            event.register(KeyBinding.ADRENALINE_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll(new ResourceLocation("rage"), RageHudOverlay.HUD_RAGE);
            event.registerAboveAll(new ResourceLocation("adrenaline"), AdrenalineHudOverlay.HUD_ADRENALINE);
        }
    }

    @Mod.EventBusSubscriber(modid = ExpertMode.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RenderEvents {
        @SubscribeEvent
        public static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(EnderDragonHeadModel.LAYER_LOCATION, EnderDragonHeadModel::createBodyLayer);
        }
    }
}
