package net.dndats.hackersandslashers.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.ModData;
import net.dndats.hackersandslashers.network.NetworkHandler;
import net.dndats.hackersandslashers.utils.PlayerUtils;
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
public record PlayerBlockPacket(boolean isBlocking) implements CustomPacketPayload {

    public static final Type<PlayerBlockPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "is_blocking"));

    public static final StreamCodec<FriendlyByteBuf, PlayerBlockPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    PlayerBlockPacket::isBlocking,
                    PlayerBlockPacket::new
            );

    private static void handlePlayerBlockPacket(final PlayerBlockPacket packet, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) context.player();
                player.setData(ModData.IS_BLOCKING, packet.isBlocking());
                player.connection.send(new PlayerBlockPacket(packet.isBlocking()));
                if (packet.isBlocking()) {
                    PlayerUtils.addSpeedModifier(player);
                    SoundEffects.playBlockSwingSound(player);
                } else {
                    PlayerUtils.removeSpeedModifier(player);
                }
            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        } else {
            context.enqueueWork(() -> {
                context.player().setData(ModData.IS_BLOCKING, packet.isBlocking());
            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        NetworkHandler.addNetworkMessage(PlayerBlockPacket.TYPE, PlayerBlockPacket.STREAM_CODEC, PlayerBlockPacket::handlePlayerBlockPacket);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
