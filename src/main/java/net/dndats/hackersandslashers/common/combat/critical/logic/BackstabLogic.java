package net.dndats.hackersandslashers.common.combat.critical.logic;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.common.combat.critical.manager.ICriticalLogic;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.minecraft.world.entity.LivingEntity;
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
                LivingEntity targetEntity = event.getEntity();
                if (canBeApplied(player, targetEntity)) {
                    // If passes the conditional, then applies the critical
                    float finalAmount = CombatUtils.dealCriticalDamage(getDamageMultiplier(), event);
                    VisualEffects.spawnCriticalParticle(targetEntity.level(), targetEntity.getX(), targetEntity.getEyeY() + 1, targetEntity.getZ(), (int) finalAmount);
                    SoundEffects.playBackstabSound(event.getEntity());
                    HackersAndSlashers.LOGGER.info("Dealt {} damage with multiplier of {}",
                            event.getAmount(),
                            getDamageMultiplier());
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to implement backstab logics: {}", e.getMessage());
        }
    }

    /**
     * Verifies if the backstab can be applied based on the position of the player and the target.
     * Also, applies the backstab if it's not being actively targeted (for mobs).
     * @param player The player attempting the backstab.
     * @param target The target entity.
     * @return True if the backstab can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Player player, LivingEntity target) {
        if (target instanceof Player) {
            return EntityUtils.isBehind(player, target)
                    && !EntityUtils.isAwareOf(player, target);
        }
        return !EntityUtils.isBeingTargeted(player, target);
    }

    private float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
