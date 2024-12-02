package net.dndats.hackersandslashers.combat.critical.manager;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.ArrayList;
import java.util.List;

// MANAGES CRITICAL TYPES REGISTERED
public class CriticalRegistry {

    private static final List<CriticalAttack> criticalTypes = new ArrayList<>();
    
    public static void registerCritical(CriticalAttack type) {
        criticalTypes.add(type);
    }

    // Applies depending on the situation and conditions
    public static void processCriticalHit(LivingIncomingDamageEvent event) {
        try {
            for (CriticalAttack critical : criticalTypes) {
                critical.getLogic().apply(event);
                HackersAndSlashers.LOGGER.info("Critical hit of type {} applied to the entity {}",
                        critical.getName(),
                        event.getEntity().getName().getString());
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to process critical: {}", e.getMessage());
        }
    }

}
