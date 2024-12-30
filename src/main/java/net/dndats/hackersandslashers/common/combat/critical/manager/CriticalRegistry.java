package net.dndats.hackersandslashers.common.combat.critical.manager;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.ArrayList;
import java.util.List;

// MANAGES CRITICAL TYPES REGISTERED
public class CriticalRegistry {

    /**
     * This class is a registerer for the critical types, registering them at the list above:
     */

    private static final List<CriticalAttack> criticalTypes = new ArrayList<>();

    /**
     * Registers a critical type at the list
     * @param type Carries a Critical Attack
     */

    public static void registerCritical(CriticalAttack type) {
        criticalTypes.add(type);
    }

    /**
     * This method is important! It intermediates the critical attack to the list.
     * It searches for all critical types applicable on that condition and call the applier of the respective critical types.
     * @param event Holds an attack event
     */
    public static void processCriticalHit(LivingIncomingDamageEvent event) {
        try {
            for (CriticalAttack critical : criticalTypes) {
                critical.getLogic().apply(event);
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to process critical: {}", e.getMessage());
        }
    }

}
