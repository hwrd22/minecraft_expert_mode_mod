package com.hwrd22.hwrd22expertmode.networking;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.networking.packet.AdrenalineDataSyncS2CPacket;
import com.hwrd22.hwrd22expertmode.networking.packet.AdrenalineUseC2SPacket;
import com.hwrd22.hwrd22expertmode.networking.packet.RageDataSyncS2CPacket;
import com.hwrd22.hwrd22expertmode.networking.packet.RageUseC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(ExpertMode.MODID, "messages")).networkProtocolVersion(() -> "1.0").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE = net;

        net.messageBuilder(RageUseC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(RageUseC2SPacket::new).encoder(RageUseC2SPacket::toBytes).consumerMainThread(RageUseC2SPacket::handle).add();
        net.messageBuilder(AdrenalineUseC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(friendlyByteBuf -> new AdrenalineUseC2SPacket()).encoder(AdrenalineUseC2SPacket::toBytes).consumerMainThread(AdrenalineUseC2SPacket::handle).add();

        net.messageBuilder(RageDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(RageDataSyncS2CPacket::new).encoder(RageDataSyncS2CPacket::toBytes).consumerMainThread(RageDataSyncS2CPacket::handle).add();
        net.messageBuilder(AdrenalineDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(AdrenalineDataSyncS2CPacket::new).encoder(AdrenalineDataSyncS2CPacket::toBytes).consumerMainThread(AdrenalineDataSyncS2CPacket::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
