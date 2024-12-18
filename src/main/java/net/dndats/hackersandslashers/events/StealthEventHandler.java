package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.combat.mechanics.stealth.Stealth;
import net.dndats.hackersandslashers.network.packets.PlayerDetectionStatePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static net.dndats.hackersandslashers.common.ModData.IS_HIDDEN;

@EventBusSubscriber(modid = "hackersandslashers")
public class StealthEventHandler {

    @SubscribeEvent
    public static void stealthCancellerCondition(LivingChangeTargetEvent event) {
        try {
            Stealth.mobsIgnoreStealthyTarget(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to cancel entity target: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void resetMobAlert(EntityTickEvent.Pre event) {
        try {
            Stealth.resetMobTarget(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reset entity data (IS_ALERT): {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void setBeingTargeted(PlayerTickEvent.Pre event) {
        try {
            Player player = event.getEntity();
            if (Stealth.mobTargetChecker(player)) {
                if (!player.getData(IS_HIDDEN)) {
                    PacketDistributor.sendToServer(new PlayerDetectionStatePacket(true));
                    player.sendSystemMessage(Component.literal("Is hidden: " + player.getData(IS_HIDDEN)));
                }
            } else {
                if (player.getData(IS_HIDDEN)) {
                    PacketDistributor.sendToServer(new PlayerDetectionStatePacket(false));
                    player.sendSystemMessage(Component.literal("Is hidden: " + player.getData(IS_HIDDEN)));
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to change player data: {}", e.getMessage());
        }
    }

}
