package net.dndats.hackersandslashers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class PlayerBlockPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PlayerBlockPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "is_blocking"));

    private final boolean isBlocking;

    public PlayerBlockPacket(boolean isBlocking) {
        this.isBlocking = isBlocking;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public static final StreamCodec<FriendlyByteBuf, PlayerBlockPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    PlayerBlockPacket::isBlocking,
                    PlayerBlockPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
