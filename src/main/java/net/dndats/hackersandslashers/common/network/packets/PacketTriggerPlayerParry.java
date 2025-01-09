package net.dndats.hackersandslashers.common.network.packets;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.utils.TickScheduler;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.setup.ModPlayerData;
import net.dndats.hackersandslashers.common.data.IsParryingData;
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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PacketTriggerPlayerParry(IsParryingData data, int duration) implements CustomPacketPayload {

    public static final Type<PacketTriggerPlayerParry> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_parry_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketTriggerPlayerParry> STREAM_CODEC = StreamCodec
            .of((RegistryFriendlyByteBuf buffer, PacketTriggerPlayerParry packet) -> {
                buffer.writeNbt(packet.data().serializeNBT(buffer.registryAccess()));
                buffer.writeInt(packet.duration());
            }, (RegistryFriendlyByteBuf buffer) -> {
                IsParryingData data = new IsParryingData();
                data.deserializeNBT(buffer.registryAccess(), Objects.requireNonNull(buffer.readNbt()));
                int duration = buffer.readInt();
                return new PacketTriggerPlayerParry(data, duration);
            });

    public static void handleData(final PacketTriggerPlayerParry message, final IPayloadContext context) {
        if (context.flow().isServerbound() && message.data() != null) {
            context.enqueueWork(() -> {
                context.player().getData(ModPlayerData.IS_PARRYING).deserializeNBT(context.player().registryAccess(),
                        message.data().serializeNBT(context.player().registryAccess()));
                context.player().setData(ModPlayerData.IS_PARRYING, message.data());
                if (message.data.getIsParrying()) {
                    SoundEffects.playParrySwingSound(context.player());
                    PlayerHelper.addSpeedModifier(context.player(), context.player().getMainHandItem());
                    TickScheduler.schedule(() -> {
                        PlayerHelper.removeSpeedModifier(context.player());
                        Objects.requireNonNull(context.player().setData(ModPlayerData.IS_PARRYING, message.data())).setIsParrying(false);
                        context.player().getData(ModPlayerData.IS_PARRYING).syncData(context.player());
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
                    var parryingData = player.getData(ModPlayerData.IS_PARRYING);
                    parryingData.deserializeNBT(player.registryAccess(), message.data().serializeNBT(player.registryAccess()));
                    player.setData(ModPlayerData.IS_PARRYING, parryingData);
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
                PacketTriggerPlayerParry.TYPE,
                PacketTriggerPlayerParry.STREAM_CODEC,
                PacketTriggerPlayerParry::handleData
        );
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
