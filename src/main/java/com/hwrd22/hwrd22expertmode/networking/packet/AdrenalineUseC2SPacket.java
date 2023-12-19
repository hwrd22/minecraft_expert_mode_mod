package com.hwrd22.hwrd22expertmode.networking.packet;

import com.hwrd22.hwrd22expertmode.adrenaline.PlayerAdrenalineProvider;
import com.hwrd22.hwrd22expertmode.networking.ModMessages;
import com.hwrd22.hwrd22expertmode.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.NetworkEvent;

public class AdrenalineUseC2SPacket {
    public AdrenalineUseC2SPacket() {
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            assert player != null;
            ServerLevel level = player.serverLevel();
            player.getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(adrenaline -> {
                if (adrenaline.getAdrenaline() == 600) {
                    level.playSound(null, player.getOnPos(), ModSounds.ADRENALINE_USED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    adrenaline.useAdrenaline();
                }
                else {
                    assert Minecraft.getInstance().player != null;
                    Minecraft.getInstance().player.displayClientMessage(Component.literal("The Adrenaline Meter is not full.").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD), true);
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), player);
                }
            });
        });
    }
}
