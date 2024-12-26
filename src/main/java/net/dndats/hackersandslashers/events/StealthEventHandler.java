package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.combat.mechanics.stealth.Stealth;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

// IMPLEMENTS STEALTH LOGICS
@EventBusSubscriber(modid = HackersAndSlashers.MODID)
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
            EntityUtils.resetMobTarget(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reset entity data (IS_ALERT): {}", e.getMessage());
        }
    }

    private static int scheduledTracker = 0;
    @SubscribeEvent
    public static void mobTargetTracker(PlayerTickEvent.Pre event) {
        try {
            if (event.getEntity().level().isClientSide) return;
            scheduledTracker++;
            if (scheduledTracker >= 20) {
                HackersAndSlashers.LOGGER.info("Tracking alert mobs");
                scheduledTracker = 0;
                Stealth.detectBeingTargeted(event.getEntity());
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to change player data: {}", e.getMessage());
        }
    }

}
