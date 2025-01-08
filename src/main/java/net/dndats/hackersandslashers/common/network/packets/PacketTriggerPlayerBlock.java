package net.dndats.hackersandslashers.common.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.utils.TickScheduler;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.dndats.hackersandslashers.common.data.IsBlockingData;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PacketTriggerPlayerBlock(IsBlockingData data, int duration) implements CustomPacketPayload {

    public static final Type<PacketTriggerPlayerBlock> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_block_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketTriggerPlayerBlock> STREAM_CODEC = StreamCodec
            .of((RegistryFriendlyByteBuf buffer, PacketTriggerPlayerBlock packet) -> {
                buffer.writeNbt(packet.data().serializeNBT(buffer.registryAccess()));
                buffer.writeInt(packet.duration());
            }, (RegistryFriendlyByteBuf buffer) -> {
                IsBlockingData data = new IsBlockingData();
                data.deserializeNBT(buffer.registryAccess(), Objects.requireNonNull(buffer.readNbt()));
                int duration = buffer.readInt();
                return new PacketTriggerPlayerBlock(data, duration);
            });

    public static void handleData(final PacketTriggerPlayerBlock message, final IPayloadContext context) {
        if (context.flow().isServerbound() && message.data() != null) {
            context.enqueueWork(() -> {
                context.player().getData(ModPlayerData.IS_BLOCKING).deserializeNBT(context.player().registryAccess(),
                        message.data().serializeNBT(context.player().registryAccess()));
                context.player().setData(ModPlayerData.IS_BLOCKING, message.data());
                if (message.data.getIsBlocking()) {
                    SoundEffects.playBlockSwingSound(context.player());
                    PlayerHelper.addSpeedModifier(context.player());
                    HackersAndSlashers.LOGGER.info("Player data IS BLOCKING set to {} at {} for player {}",
                            context.player().getData(ModPlayerData.IS_BLOCKING).getIsBlocking(), context.flow().getReceptionSide(), context.player().getDisplayName());
                    TickScheduler.schedule(() -> {
                        PlayerHelper.removeSpeedModifier(context.player());
                        Objects.requireNonNull(context.player().setData(ModPlayerData.IS_BLOCKING, message.data())).setIsBlocking(false);
                        context.player().getData(ModPlayerData.IS_BLOCKING).syncData(context.player());
                        HackersAndSlashers.LOGGER.info("Now, player data IS BLOCKING has been set to {} at {} for player {}",
                                context.player().getData(ModPlayerData.IS_BLOCKING).getIsBlocking(), context.flow().getReceptionSide(), context.player().getDisplayName());
                    }, message.duration());
                }
            }).exceptionally(e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        }
        if (context.flow().isClientbound() && message.data() != null) {
            context.enqueueWork(() -> {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    var blockingData = player.getData(ModPlayerData.IS_BLOCKING);
                    blockingData.deserializeNBT(player.registryAccess(), message.data().serializeNBT(player.registryAccess()));
                    player.setData(ModPlayerData.IS_BLOCKING, blockingData);
                    HackersAndSlashers.LOGGER.info("Client received updated blocking data: {}", blockingData.getIsBlocking());
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
