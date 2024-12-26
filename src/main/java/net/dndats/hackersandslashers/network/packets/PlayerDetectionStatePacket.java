package net.dndats.hackersandslashers.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.ModData;
import net.dndats.hackersandslashers.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PlayerDetectionStatePacket(int visibility_level) implements CustomPacketPayload {

    public static final Type<PlayerDetectionStatePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "is_hidden"));

    public static final StreamCodec<FriendlyByteBuf, PlayerDetectionStatePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    PlayerDetectionStatePacket::visibility_level,
                    PlayerDetectionStatePacket::new
            );

    private static void handlePlayerDetectionStatePacket(final PlayerDetectionStatePacket packet, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                player.setData(ModData.VISIBILITY_LEVEL, packet.visibility_level);
                player.connection.send(new PlayerDetectionStatePacket(packet.visibility_level()));
            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        } else {
            context.enqueueWork(() -> {
                context.player().setData(ModData.VISIBILITY_LEVEL, packet.visibility_level());
            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        NetworkHandler.addNetworkMessage(PlayerDetectionStatePacket.TYPE, PlayerDetectionStatePacket.STREAM_CODEC, PlayerDetectionStatePacket::handlePlayerDetectionStatePacket);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
