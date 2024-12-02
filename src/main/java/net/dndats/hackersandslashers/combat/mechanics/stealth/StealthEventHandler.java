package net.dndats.hackersandslashers.combat.mechanics.stealth;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

// HANDLER: STEALTH
@EventBusSubscriber(modid = "hackersandslashers")
public class StealthEventHandler {

    // Implements stealth logics from Players : Mobs
    @SubscribeEvent
    public static void onEntitySetTarget(LivingChangeTargetEvent event) {
        try {
            if (event.getNewAboutToBeSetTarget() != null) {
                Stealth.mobsIgnoreStealthyTarget(event);
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Failing applying stealth logics: {}", e.getMessage());
        }
    }

}
