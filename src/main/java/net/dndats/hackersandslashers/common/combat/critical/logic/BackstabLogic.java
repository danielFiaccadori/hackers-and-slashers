package net.dndats.hackersandslashers.common.combat.critical.logic;

import net.dndats.hackersandslashers.common.combat.critical.interfaces.ICriticalLogic;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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
     * Verifies if the backstab can be applied based on the position of the player and the target.
     * Also, applies the backstab if it's not being actively targeted (for mobs).
     * @param source The player attempting the backstab.
     * @param target The target entity.
     * @return True if the backstab can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Entity source, LivingEntity target) {
        if (!(source instanceof Player player)) return false;
        if (target instanceof Player) {
            return EntityUtils.isBehind(player, target)
                    && !EntityUtils.isAwareOf(player, target);
        } else {
            return !EntityUtils.isBeingTargeted(player, target);
        }
    }

    @Override
    public float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
