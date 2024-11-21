package net.dndats.hackersandslashers.combat.critical.types;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.combat.critical.CriticalAttack;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// CRITICAL ATTACK OF TYPE BACKSTAB
public class Backstab extends CriticalAttack {

    public Backstab(String name, Float damageMultiplier) {
        super(name, damageMultiplier);
    }

    @Override
    public void applyCritical(LivingIncomingDamageEvent event) {
        try {
            // Verifies if the source is a player
            if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof Mob mob) {
                // If the entity is not being actively targeted, applies the backstab effect
                if (!EntityUtils.isBeingTargeted(mob, player)) {
                    CombatUtils.dealCriticalDamage(getDamageMultiplier(), event);
                    HackersAndSlashers.LOGGER.info("Dealt {} damage with multiplier of {}",
                            event.getAmount(),
                            getDamageMultiplier());
                }

            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to implement backstab logics: {}", e.getMessage());
        }
    }

}
