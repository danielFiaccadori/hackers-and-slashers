package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.combat.mechanics.stealth.Stealth;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

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
            EntityUtils.resetMobTarget(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reset entity data (IS_ALERT): {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void triggerHiddenStatus(PlayerTickEvent.Pre event) {
        try {
            Stealth.detectBeingTargeted(event.getEntity());
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to change player data: {}", e.getMessage());
        }
    }

}
