package net.dndats.hackersandslashers.client.animations;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.network.packets.PlayerAnimationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Objects;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PlayerAnimator {

    /**
     * Registers an Animation Factory at FMLClientSetupEvent
     * @param event The event registerer.
     */

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animations"),
                42,
                PlayerAnimator::registerPlayerAnimation
        );
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
            if (entity == null) return;
            if (world.isClientSide()) {
                if (entity instanceof AbstractClientPlayer player) {
                    var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.
                            getPlayerAssociatedData(Minecraft.getInstance().player).
                            get(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animations"));
                    if (animation != null) {
                        animation.replaceAnimationWithFade(AbstractFadeModifier
                                        .functionalFadeIn(20, (modelName, type, value) -> value), Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, animationName))).
                                        playAnimation().setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(false).setShowLeftItem(true)));
                    }
                }
            }
            if (!world.isClientSide()) {
                if (entity instanceof Player) {
                    PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayerAnimationPacket(animationName, entity.getId(), true));
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error at PlayerAnimator::playAnimation: {}", e.getMessage());
        }
    }

    /**
     * This method registers an animation on the client side.
     * @param player The player to register an animation.
     * @return a modifier layer
     */

    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }

}
