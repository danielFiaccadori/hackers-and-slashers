package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.combat.critical.manager.CriticalRegistry;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// HANDLER: COMBAT RELATED THINGS
@EventBusSubscriber(modid = "hackersandslashers")
public class CombatEventHandler {

    // These methods communicate with the deeper layers, delegating the implementation of the necessary logic

    // Critical hit handler
    @SubscribeEvent
    public static void dealCriticalHit(LivingIncomingDamageEvent event) {
        try {
            //Apply critical hit
            CriticalRegistry.processCriticalHit(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical hit: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void blockReduceDamage(LivingIncomingDamageEvent event) {
        try {
            CombatUtils.blockDamage(25, event);
            HackersAndSlashers.LOGGER.info("Reduced {} damage of {} from entity {}", event.getAmount(), event.getOriginalAmount(), event.getSource().getEntity());
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to block damage: {}", e.getMessage());
        }
    }

}
