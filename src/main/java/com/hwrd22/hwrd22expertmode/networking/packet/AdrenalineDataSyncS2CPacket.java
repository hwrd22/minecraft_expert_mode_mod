package com.hwrd22.hwrd22expertmode.networking.packet;

import com.hwrd22.hwrd22expertmode.client.ClientAdrenalineData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AdrenalineDataSyncS2CPacket {
    private final int adrenaline;

    public AdrenalineDataSyncS2CPacket(int adrenaline) {
        this.adrenaline = adrenaline;
    }

    public AdrenalineDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.adrenaline = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(adrenaline);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientAdrenalineData.set(adrenaline);
        });
    }
}
