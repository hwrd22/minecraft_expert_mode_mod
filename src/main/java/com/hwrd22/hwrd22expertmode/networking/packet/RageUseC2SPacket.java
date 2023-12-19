package com.hwrd22.hwrd22expertmode.networking.packet;

import com.hwrd22.hwrd22expertmode.networking.ModMessages;
import com.hwrd22.hwrd22expertmode.rage.PlayerRageProvider;
import com.hwrd22.hwrd22expertmode.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RageUseC2SPacket {
    public RageUseC2SPacket() {
    }

    public RageUseC2SPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            player.getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(rage -> {
                if (rage.getRage() == 10000) {
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 180, 4));
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 180, 1));
                    level.playSound(null, player.getOnPos(), ModSounds.RAGE_USED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    rage.useRage();
                }
                else {
                    Minecraft.getInstance().player.displayClientMessage(Component.literal("The Rage Meter is not full.").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD), true);
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), player);
                }
            });

        });
    }
}
