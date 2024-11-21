package net.dndats.hackersandslashers.combat.critical;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.ArrayList;
import java.util.List;

// MANAGES CRITICAL TYPES REGISTERED
public class CriticalRegistry {

    private static final List<CriticalHit> criticalTypes = new ArrayList<>();

    public static void registerCritical(CriticalHit type) {
        criticalTypes.add(type);
    }

    // Applies depending on the situation and conditions
    public static CriticalHit processCriticalHit(LivingIncomingDamageEvent event) {
        try {
            for (CriticalHit critical : criticalTypes) {
                critical.applyCritical(event);
                return critical;
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to process critical: {}", e.getMessage());
        }
        return null;
    }

}
