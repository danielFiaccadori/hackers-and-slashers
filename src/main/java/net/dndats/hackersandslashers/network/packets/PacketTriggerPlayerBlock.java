package net.dndats.hackersandslashers.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.dndats.hackersandslashers.common.data.IsBlockingData;
import net.dndats.hackersandslashers.network.NetworkHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PacketTriggerPlayerBlock(IsBlockingData data) implements CustomPacketPayload {

    public static final Type<PacketTriggerPlayerBlock> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_block_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketTriggerPlayerBlock> STREAM_CODEC = StreamCodec
            .of((RegistryFriendlyByteBuf buffer, PacketTriggerPlayerBlock packet) ->
                    buffer.writeNbt(packet.data().serializeNBT(buffer.registryAccess())), (RegistryFriendlyByteBuf buffer) -> {
                        PacketTriggerPlayerBlock message = new PacketTriggerPlayerBlock(new IsBlockingData());
                        message.data().deserializeNBT(buffer.registryAccess(), buffer.readNbt());
                        return message;
            });

    public static void handleData(final PacketTriggerPlayerBlock message, final IPayloadContext context) {
        if (context.flow().isClientbound() && message.data() != null) {
            context.enqueueWork(() -> context.player().getData(ModPlayerData.IS_BLOCKING).deserializeNBT(context.player().registryAccess(),
                    message.data().serializeNBT(context.player().registryAccess()))).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        NetworkHandler.addNetworkMessage(
                PacketTriggerPlayerBlock.TYPE,
                PacketTriggerPlayerBlock.STREAM_CODEC,
                PacketTriggerPlayerBlock::handleData
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
