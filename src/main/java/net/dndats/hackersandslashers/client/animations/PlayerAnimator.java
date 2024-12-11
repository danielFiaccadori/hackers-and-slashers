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

// Class responsible b
@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PlayerAnimator {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animation"),
                42,
                PlayerAnimator::registerPlayerAnimation
        );
    }

    /*
    public static void playAnimation(LevelAccessor world, Entity entity, String animationName) {
        execute(null, world, entity, animationName);
    }
     */

    public static void execute(@Nullable Event event, LevelAccessor world, Entity entity, String animationName) {
        if (entity == null) return;
        if (world.isClientSide()) {
            if (entity instanceof AbstractClientPlayer player) {
                var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player).get(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_animation"));
                if (animation != null) {
                    animation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value), PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, animationName)).
                            playAnimation().setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftItem(false)));
                }
            }
        }
        if (!world.isClientSide()) {
            if (entity instanceof Player) {
                PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayerAnimationPacket(animationName, entity.getId(), true));
            }
        }
    }

    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }

}
