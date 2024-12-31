package net.dndats.hackersandslashers.common.network.packets;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PacketPlayAnimationAtPlayer(String animationName, Integer entityId, boolean override) implements CustomPacketPayload{

    public static final CustomPacketPayload.Type<PacketPlayAnimationAtPlayer> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "sync_clients_animation"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketPlayAnimationAtPlayer> STREAM_CODEC =
        StreamCodec.of((RegistryFriendlyByteBuf buffer, PacketPlayAnimationAtPlayer message) -> {
            buffer.writeUtf(message.animationName);
            buffer.writeInt(message.entityId);
            buffer.writeBoolean(message.override);
        }, (RegistryFriendlyByteBuf buffer) -> new PacketPlayAnimationAtPlayer(buffer.readUtf(), buffer.readInt(), buffer.readBoolean()));

    public static void handleData(final PacketPlayAnimationAtPlayer message, final IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (Minecraft.getInstance().player == null || level == null) return;
                if (level.getEntity(message.entityId()) != null) {
                    Player player = (Player) level.getEntity(message.entityId());
                    if (player == Minecraft.getInstance().player) return;
                    if (player instanceof AbstractClientPlayer clientPlayer) {
                        Object associatedData = PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(ResourceLocation
                                .fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animations"));
                        if (associatedData instanceof ModifierLayer<?> modifierLayer) {
                            @SuppressWarnings("unchecked")
                            var animation = (ModifierLayer<IAnimation>) modifierLayer;
                            animation.replaceAnimationWithFade(
                                    AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value),
                                    Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, message.animationName())))
                                            .playAnimation()
                                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                                            .setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(false).setShowLeftItem(true)));
                            HackersAndSlashers.LOGGER.info("Animation '{}' played for all clients at player '{}'.", message.animationName(), clientPlayer.getName().getString());
                        }
                    }
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
                PacketPlayAnimationAtPlayer.TYPE,
                PacketPlayAnimationAtPlayer.STREAM_CODEC,
                PacketPlayAnimationAtPlayer::handleData
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
