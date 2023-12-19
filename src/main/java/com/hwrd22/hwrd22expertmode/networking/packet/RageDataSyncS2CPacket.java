package com.hwrd22.hwrd22expertmode.networking.packet;

import com.hwrd22.hwrd22expertmode.client.ClientRageData;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RageDataSyncS2CPacket {
    private final int rage;

    public RageDataSyncS2CPacket(int rage) {
        this.rage = rage;
    }

    public RageDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.rage = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(rage);
    }

    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> ClientRageData.set(rage));
    }
}
