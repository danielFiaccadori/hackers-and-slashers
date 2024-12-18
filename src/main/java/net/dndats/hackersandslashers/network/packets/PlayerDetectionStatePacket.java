package net.dndats.hackersandslashers.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static net.dndats.hackersandslashers.common.ModData.IS_HIDDEN;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PlayerDetectionStatePacket(boolean isHidden) implements CustomPacketPayload {

    public static final Type<PlayerDetectionStatePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "is_hidden"));

    public static final StreamCodec<FriendlyByteBuf, PlayerDetectionStatePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    PlayerDetectionStatePacket::isHidden,
                    PlayerDetectionStatePacket::new
            );

    private static void handlePlayerDetectionStatePacket(final PlayerDetectionStatePacket packet, final IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        player.setData(IS_HIDDEN, packet.isHidden);
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
