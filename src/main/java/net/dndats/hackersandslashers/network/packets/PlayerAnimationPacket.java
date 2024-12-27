package net.dndats.hackersandslashers.network.packets;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public record PlayerAnimationPacket(String animation, int target, boolean override) implements CustomPacketPayload {

    public static final Type<PlayerAnimationPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "playeranimator"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerAnimationPacket> STREAM_CODEC =
            StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayerAnimationPacket message) -> {
                buffer.writeUtf(message.animation);
                buffer.writeInt(message.target);
                buffer.writeBoolean(message.override);
    }, (RegistryFriendlyByteBuf buffer) -> new PlayerAnimationPacket(buffer.readUtf(), buffer.readInt(), buffer.readBoolean()));

    public static void handlePlayerAnimationPacket(final PlayerAnimationPacket packet, final IPayloadContext context) {
        if (context.flow() == PacketFlow.CLIENTBOUND) {
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().player.level();
                if (level.getEntity(packet.target) != null) {
                    Player player = (Player) level.getEntity(packet.target);
                    if (player instanceof AbstractClientPlayer player_) {
                        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player).get(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animations"));
                        if (animation != null && (packet.override ? true : !animation.isActive())) {
                            animation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value), PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath("hackersandslashers", packet.animation))
                                    .playAnimation().setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftItem(false)));
                        }
                    }
                }
            }).exceptionally (e -> {
                context.connection().disconnect(Component.literal(e.getMessage()));
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        NetworkHandler.addNetworkMessage(
                PlayerAnimationPacket.TYPE,
                PlayerAnimationPacket.STREAM_CODEC,
                PlayerAnimationPacket::handlePlayerAnimationPacket
        );
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
