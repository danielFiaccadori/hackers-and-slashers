package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.combat.critical.CriticalHit;
import net.dndats.hackersandslashers.combat.critical.CriticalRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// COMBAT CONTROLLER
@EventBusSubscriber(modid = "hackersandslashers")
public class CombatEvents {

    //This method communicates with the deeper layers, delegating the implementation of the necessary logic
    @SubscribeEvent
    public static void onEntityHurt(LivingIncomingDamageEvent event) {
        try {
            if (event.getSource().getEntity() instanceof Player) {
                //Apply critical hit if viable
                CriticalHit criticalHit = CriticalRegistry.processCriticalHit(event);
                HackersAndSlashers.LOGGER.info("Critical hit of type {} applied to the entity {}",
                        criticalHit.getName(),
                        event.getEntity().getName().getString());
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error on entity hurt subscribed event: {}", e.getMessage());
        }
    }

}
