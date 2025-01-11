package net.dndats.hackersandslashers.common.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PacketServerPlayAnimation(String animationName) implements CustomPacketPayload {

    public static final Type<PacketServerPlayAnimation> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "sync_server_animation"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketServerPlayAnimation> STREAM_CODEC =
            StreamCodec.of((RegistryFriendlyByteBuf buffer, PacketServerPlayAnimation message) ->
                    buffer.writeUtf(message.animationName()), (RegistryFriendlyByteBuf buffer) -> new PacketServerPlayAnimation(buffer.readUtf()));


    public static void handleData(final PacketServerPlayAnimation message, final IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                if (!context.player().level().isClientSide()) {
                    PlayerAnimator.playAnimation(context.player().level(), context.player(), message.animationName());
                }
            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });

        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        NetworkHandler.addNetworkMessage(
                PacketServerPlayAnimation.TYPE,
                PacketServerPlayAnimation.STREAM_CODEC,
                PacketServerPlayAnimation::handleData
        );
    }

    @Override
    public @NotNull Type<PacketServerPlayAnimation> type() {
        return TYPE;
    }


}
