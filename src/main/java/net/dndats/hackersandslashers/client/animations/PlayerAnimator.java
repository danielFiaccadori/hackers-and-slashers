package net.dndats.hackersandslashers.client.animations;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.network.packets.PacketPlayAnimationAtPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;

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
     * @param world The world/level the animation will be played.
     * @param entity The target entity.
     * @param animationName The animation to be played.
     */

    public static void playAnimation(LevelAccessor world, Entity entity, String animationName) {
        try {
            if (entity == null) return;
            if (world.isClientSide()) {
                if (Minecraft.getInstance().player == null) return;
                if (entity instanceof AbstractClientPlayer clientPlayer) {
                    Object associatedData = PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player).get(ResourceLocation
                            .fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animations"));
                    if (associatedData instanceof ModifierLayer<?> modifierLayer) {
                        @SuppressWarnings("unchecked")
                        ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) modifierLayer;
                        if (!animation.isActive()) {
                            animation.replaceAnimationWithFade(
                                    AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value),
                                    Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, animationName)))
                                            .playAnimation()
                                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                                            .setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(false).setShowLeftItem(true)));
                            HackersAndSlashers.LOGGER.info("Animation '{}' played at client side for clientPlayer '{}'.", animationName, clientPlayer.getName().getString());
                        }
                    }
                }
            }
            if (!world.isClientSide()) {
                if (entity instanceof Player) {
//                    PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PacketPlayAnimationAtPlayer(animationName, entity.getId(), false));
                    PacketDistributor.sendToPlayersTrackingEntity(entity, new PacketPlayAnimationAtPlayer(animationName, entity.getId(), false));
                    HackersAndSlashers.LOGGER.info("The server sent a message to all clients in the ServerLevel");
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error in PlayerAnimator::playAnimation: {}", e.getMessage(), e);
        }
    }

}
