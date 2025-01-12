package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.stealth.Stealth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class StealthHandler {

    /**
     * Handles all events related to the stealth mechanic
     */

    @SubscribeEvent
    public static void handleStealthBehavior(LivingChangeTargetEvent event) {
        try {
            Stealth.stealthBehavior(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to cancel entity target: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void increaseAlertLevelOnHit(LivingIncomingDamageEvent event) {
        try {
            Stealth.increaseAlertLevelOnHit(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to increase alert level on hit: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void handleMobAlert(EntityTickEvent.Pre event) {
        try {
            Stealth.mobAlert(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reset entity data (IS_ALERT): {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void resetScheduledTracker(PlayerEvent.PlayerLoggedInEvent event) {
        scheduledTracker = 0;
    }

    private static int scheduledTracker = 0;
    @SubscribeEvent
    public static void mobTargetTracker(PlayerTickEvent.Pre event) {
        try {
            if (!event.getEntity().level().isClientSide) {
                scheduledTracker++;
                if (scheduledTracker >= 10) {
                    scheduledTracker = 0;
                    for (Player player : event.getEntity().level().players()) {
                        Stealth.updatePlayerVisibility(player);
                    }
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to change player data: {}", e.getMessage());
        }
    }

}
