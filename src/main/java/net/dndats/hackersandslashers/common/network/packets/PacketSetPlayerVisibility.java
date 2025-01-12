package net.dndats.hackersandslashers.common.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.common.data.VisibilityLevelData;
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

import java.util.Objects;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PacketSetPlayerVisibility(VisibilityLevelData data) implements CustomPacketPayload {

    public static final Type<PacketSetPlayerVisibility> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_visibility_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSetPlayerVisibility> STREAM_CODEC = StreamCodec
            .of((RegistryFriendlyByteBuf buffer, PacketSetPlayerVisibility packet) ->
                    buffer.writeNbt(packet.data().serializeNBT(buffer.registryAccess())), (RegistryFriendlyByteBuf buffer) -> {
                        PacketSetPlayerVisibility message = new PacketSetPlayerVisibility(new VisibilityLevelData());
                        message.data().deserializeNBT(buffer.registryAccess(), Objects.requireNonNull(buffer.readNbt()));
                        return message;
            });

    public static void handleData(final PacketSetPlayerVisibility message, final IPayloadContext context) {
        if (context.flow().isClientbound() && message.data() != null) {
            context.enqueueWork(() -> {
                context.player().getData(ModData.VISIBILITY_LEVEL).deserializeNBT(context.player().registryAccess(),
                    message.data().serializeNBT(context.player().registryAccess()));
                context.player().setData(ModData.VISIBILITY_LEVEL, message.data());
            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        NetworkHandler.addNetworkMessage(
                PacketSetPlayerVisibility.TYPE,
                PacketSetPlayerVisibility.STREAM_CODEC,
                PacketSetPlayerVisibility::handleData
        );
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
