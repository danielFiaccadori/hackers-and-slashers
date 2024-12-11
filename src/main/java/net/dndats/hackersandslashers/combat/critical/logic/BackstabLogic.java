package net.dndats.hackersandslashers.combat.critical.logic;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.combat.critical.manager.ICriticalLogic;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// CRITICAL ATTACK OF TYPE BACKSTAB
public class BackstabLogic implements ICriticalLogic {

    private final float DAMAGE_MULTIPLIER;

    /**
     * When created in main class, specify the amount of damage that this critical hit does.
     * @param damageMultiplier The multiplier of this critical hit.
     */

    public BackstabLogic(float damageMultiplier) {
        if (damageMultiplier <= 0) {
            throw new IllegalArgumentException("Damage multiplier must be greater than 0");
        }
        DAMAGE_MULTIPLIER = damageMultiplier;
    }

    /**
     * Applies, if viable, the backstab critical.
     * @param event The damage event that multiplies the damage.
     */

    @Override
    public void apply(LivingIncomingDamageEvent event) {
        try {
            // Verifies if the source is a player
            if (event.getSource().getEntity() instanceof Player player) {
                if (canBeApplied(player, event.getEntity())) {
                    // If the entity is not being actively targeted, applies the backstab effect
                    CombatUtils.dealCriticalDamage(getDamageMultiplier(), event);
                }
                SoundEffects.playCriticalSound(event.getEntity());
                HackersAndSlashers.LOGGER.info("Dealt {} damage with multiplier of {}",
                        event.getAmount(),
                        getDamageMultiplier());
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to implement backstab logics: {}", e.getMessage());
        }
    }

    /**
     * Verifies if the backstab can be applied based on the position of the player and the target.
     * Also, applies the backstab if it's not being actively targeted (for mobs).
     * @param source The player attempting the backstab.
     * @param target The target entity.
     * @return True if the backstab can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Player source, Entity target) {
        if (target instanceof Player) {
            EntityUtils.isBehind(source, target);
        }
        return !EntityUtils.isBeingTargeted(source, target);
    }

    private float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
