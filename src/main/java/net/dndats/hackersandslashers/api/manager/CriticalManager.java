package net.dndats.hackersandslashers.api.manager;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.interfaces.ICritical;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.ArrayList;
import java.util.List;

// MANAGES CRITICAL TYPES REGISTERED
public class CriticalManager {

    /// To add a new critical hit, create a new logic and an object. After that, register in the mod constructor.
    /// This class is a register and manager for the critical types, registering them at the list above:

    private static final List<ICritical> criticalTypes = new ArrayList<>();

    /**
     * Registers a critical type at the list
     * @param critical Carries a Critical hit
     */

    public static void registerCritical(ICritical critical) {
        criticalTypes.add(critical);
    }

    /**
     * This method is important! It intermediates the critical to the list.
     * It searches for all critical types applicable on that condition and call the applier of the respective critical types.
     * @param event Holds an attack event
     */

    public static boolean applyCriticalHit(LivingIncomingDamageEvent event) {
        try {
            if (event.getSource().getEntity() instanceof Player player) {
                float totalDamageMultiplier = processCriticalHit(event, player);
                if (totalDamageMultiplier > 0) {
                    float finalAmount = CombatUtils.dealCriticalDamage(totalDamageMultiplier, event);
                    HackersAndSlashers.LOGGER.info("Dealt {} damage with a total multiplier of {}",
                            finalAmount,
                            totalDamageMultiplier);
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to process critical: {}", e.getMessage());
        }
        return false;
    }

    /**
     * This helper method is used to calculate the total damage multiplier of the critical hits.
     *
     * @param event: the damage event
     * @param player: the player attempting to backstab
     * @return the final value of the multiplier
     */

    private static float processCriticalHit(LivingIncomingDamageEvent event, Player player) {
        float totalDamageMultiplier = 0;
        for (ICritical critical : criticalTypes) {
            if (critical instanceof RangedCritical rangedCritical) {
                if (rangedCritical.getLogic().canBeApplied(event.getSource().getDirectEntity(), event.getEntity())) {
                    totalDamageMultiplier += rangedCritical.getLogic().getDamageMultiplier();
                }
            }
            if (critical.getLogic().canBeApplied(player, event.getEntity())) {
                totalDamageMultiplier += critical.getLogic().getDamageMultiplier();
                if (critical.getLogic().hasAdditionalModifiers()) {
                    totalDamageMultiplier += critical.getLogic().getAdditionalModifiers(event);
                }
                critical.getLogic().applyOnHitFunction(event);
            }
        }
        return totalDamageMultiplier;
    }

}
