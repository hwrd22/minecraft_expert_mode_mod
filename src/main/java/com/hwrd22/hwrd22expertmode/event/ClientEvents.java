package com.hwrd22.hwrd22expertmode.event;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.client.AdrenalineHudOverlay;
import com.hwrd22.hwrd22expertmode.client.RageHudOverlay;
import com.hwrd22.hwrd22expertmode.networking.ModMessages;
import com.hwrd22.hwrd22expertmode.networking.packet.AdrenalineUseC2SPacket;
import com.hwrd22.hwrd22expertmode.networking.packet.RageUseC2SPacket;
import com.hwrd22.hwrd22expertmode.util.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
            event.registerAboveAll("rage", RageHudOverlay.HUD_RAGE);
            event.registerAboveAll("adrenaline", AdrenalineHudOverlay.HUD_ADRENALINE);
        }
    }
}
