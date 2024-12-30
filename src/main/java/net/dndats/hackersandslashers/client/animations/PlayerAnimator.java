package net.dndats.hackersandslashers.client.animations;

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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PlayerAnimator {

    /**
     * Setups the player animator
     * @param event The event registerer.
     */

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(((player, animationStack) -> {
            ModifierLayer<IAnimation> layer = new ModifierLayer<>();
            animationStack.addAnimLayer(69, layer);
            PlayerAnimationAccess.getPlayerAssociatedData(player)
                    .set(ResourceLocation.fromNamespaceAndPath("hackersandslashers", "player_animations"), layer);
        }));
    }

    /**
     * This method, when called, plays an animation at the entity passed as parameter.
     * It takes a nullable event, so it can be called at client side.
     * @param event The nullable event that calls the method.
     * @param world The world/level the animation will be played.
     * @param entity The target entity.
     * @param animationName The animation to be played.
     */

    public static void playAnimation(@Nullable Event event, LevelAccessor world, Entity entity, String animationName) {
        try {
            if (entity == null)
                return;
            if (world.isClientSide()) {
                if (entity instanceof AbstractClientPlayer player) {
                    var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player)
                            .get(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animations"));
                    if (animation != null && !animation.isActive()) {
                        animation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value),
                                PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, animationName))
                                        .playAnimation().setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                                        .setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(false).setShowLeftItem(true)));
                    }
                    HackersAndSlashers.LOGGER.info("Animation played at client side!");
                }
            }
            if (!world.isClientSide()) {
                if (entity instanceof Player)
                    HackersAndSlashers.LOGGER.info("Animation played at server side!");
                    PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new ModAnimationMessage(animationName, entity.getId(), false));
            }

        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error at PlayerAnimator::playAnimation: {}", e.getMessage());
        }
    }

    @EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
    public record ModPlayAnimationMessage(String animationName) implements CustomPacketPayload {

        public static final Type<ModPlayAnimationMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "play_server_animation"));

        public static final StreamCodec<RegistryFriendlyByteBuf, ModPlayAnimationMessage> STREAM_CODEC =
                StreamCodec.of((RegistryFriendlyByteBuf buffer, ModPlayAnimationMessage message) -> {
                            buffer.writeUtf(message.animationName);
                        }, (RegistryFriendlyByteBuf buffer) -> new ModPlayAnimationMessage(buffer.readUtf()));

        public static void handleData(final ModPlayAnimationMessage message, final IPayloadContext context) {
            if (context.flow().isServerbound()) {
                context.enqueueWork(() -> {
                    if (!context.player().level().hasChunkAt(context.player().blockPosition())) return;
                    playAnimation(null, context.player().level(), context.player(), message.animationName());
                });
            }
        }

        @SubscribeEvent
        public static void registerMessage(FMLCommonSetupEvent event) {
            NetworkHandler.addNetworkMessage(
                    ModPlayAnimationMessage.TYPE,
                    ModPlayAnimationMessage.STREAM_CODEC,
                    ModPlayAnimationMessage::handleData
            );
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

    }

    /**
     * This is the packet responsible by sending a message about the animation.
     * This packet should be only sent to client side at the playAnimation method. For other uses, like playing at server side, use "ModPlayAnimationMessage" instead
     *
     * @param animation: the animation to be sent
     * @param target: the target player to play the animation
     * @param override: if the animation should or not override another one
     */

    @EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
    public record ModAnimationMessage(String animation, int target, boolean override) implements CustomPacketPayload {

        public static final Type<ModAnimationMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animator"));

        public static final StreamCodec<RegistryFriendlyByteBuf, ModAnimationMessage> STREAM_CODEC =
                StreamCodec.of((RegistryFriendlyByteBuf buffer, ModAnimationMessage message) -> {
                    buffer.writeUtf(message.animation);
                    buffer.writeInt(message.target);
                    buffer.writeBoolean(message.override);
        }, (RegistryFriendlyByteBuf buffer) -> new ModAnimationMessage(buffer.readUtf(), buffer.readInt(), buffer.readBoolean()));

        public static void handleData(final ModAnimationMessage message, final IPayloadContext context) {
            try {
                if (context.flow().isClientbound()) {
                    context.enqueueWork(() -> {
                        Level level = Minecraft.getInstance().player.level();
                        if (level.getEntity(message.target) != null) {
                            Player player = (Player) level.getEntity(message.target);
                            if (player instanceof AbstractClientPlayer clientPlayer) {
                                var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player)
                                        .get(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animations"));
                                if (animation != null && (message.override || !animation.isActive())) {
                                    animation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value),
                                            PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, message.animation))
                                                    .playAnimation().setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                                                    .setFirstPersonConfiguration(new FirstPersonConfiguration()
                                                            .setShowRightArm(true)
                                                            .setShowLeftItem(false)));
                                }
                            }
                        }
                    }).exceptionally(e -> {
                        context.connection().disconnect(Component.literal(e.getMessage()));
                        return null;
                    });
                }
            } catch (Exception e) {
                HackersAndSlashers.LOGGER.error("Could not handle animation at PlayerAnimator::ModAnimationMessage: {}", e.getMessage());
            }
        }

        @SubscribeEvent
        public static void registerMessage(FMLCommonSetupEvent event) {
            NetworkHandler.addNetworkMessage(
                    ModAnimationMessage.TYPE,
                    ModAnimationMessage.STREAM_CODEC,
                    ModAnimationMessage::handleData
            );
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

    }

    /**
     * This method registers an animation on the client side.
     *
     * @param player The player to register an animation.
     * @return a modifier layer
     */

    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }

}
