package net.dndats.hackersandslashers.common.combat.critical.manager;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.common.combat.critical.interfaces.ICritical;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.ArrayList;
import java.util.List;

// MANAGES CRITICAL TYPES REGISTERED
public class CriticalRegistry {

    /**
     * This class is a registerer for the critical types, registering them at the list above:
     */

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

    public static void processCriticalHit(LivingIncomingDamageEvent event) {
        try {
            if (event.getSource().getEntity() instanceof Player player) {
            float totalDamageMultiplier = 0;
            for (ICritical critical : criticalTypes) {
                if (critical instanceof RangedCritical rangedCritical) {
                    if (rangedCritical.getLogic().canBeApplied(event.getSource().getDirectEntity(), event.getEntity())) {
                        totalDamageMultiplier += rangedCritical.getLogic().getDamageMultiplier();
                    }
                }
                if (critical.getLogic().canBeApplied(player, event.getEntity())) {
                    totalDamageMultiplier += critical.getLogic().getDamageMultiplier();
                    if (event.getEntity().hasEffect(ModMobEffects.STUN)) {
                        event.getEntity().removeEffect(ModMobEffects.STUN);
                    }
                }
            }
            if (totalDamageMultiplier > 0) {
                float finalAmount = CombatUtils.dealCriticalDamage(totalDamageMultiplier, event);
                SoundEffects.playCriticalSound(event.getEntity());
                VisualEffects.spawnCriticalParticle(event.getEntity().level(),
                        event.getEntity().getX(),
                        event.getEntity().getEyeY() + 1,
                        event.getEntity().getZ(),
                        (int) finalAmount,
                        event.getSource());
                HackersAndSlashers.LOGGER.info("Dealt {} damage with a total multiplier of {}",
                        finalAmount,
                        totalDamageMultiplier);
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to process critical: {}", e.getMessage());
        }
    }

}
