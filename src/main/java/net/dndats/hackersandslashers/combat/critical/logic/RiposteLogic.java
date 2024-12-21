package net.dndats.hackersandslashers.combat.critical.logic;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.combat.critical.manager.ICriticalLogic;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// CRITICAL ATTACK OF TYPE RIPOSTE
public class RiposteLogic implements ICriticalLogic {

    private final float DAMAGE_MULTIPLIER;

    /**
     * When created in main class, specify the amount of damage that this critical hit does.
     * @param damageMultiplier The multiplier of this critical hit.
     */

    public RiposteLogic(float damageMultiplier) {
        if (damageMultiplier <= 0) {
            throw new IllegalArgumentException("Damage multiplier must be greater than 0");
        }
        DAMAGE_MULTIPLIER = damageMultiplier;
    }

    /**
     * Applies, if viable, the riposte critical.
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
                    CombatUtils.dealCriticalDamage(getDamageMultiplier(), event);
                    SoundEffects.playCriticalSound(event.getEntity());
                    HackersAndSlashers.LOGGER.info("Dealt {} damage with multiplier of {}",
                            event.getAmount(),
                            getDamageMultiplier());
                    // Remove stun potion effect
                    if (targetEntity.hasEffect(ModMobEffects.STUN)) {
                        targetEntity.removeEffect(ModMobEffects.STUN);
                    }
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to implement riposte logics: {}", e.getMessage());
        }
    }

    /**
     * Verifies if the riposte can be applied if an entity has the Stun effect.
     * @param source The player attempting the riposte.
     * @param target The target entity.
     * @return True if the riposte can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Player source, LivingEntity target) {
        return target.hasEffect(ModMobEffects.STUN);
    }

    private float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
