package net.dndats.hackersandslashers.common.combat.critical.logic;

import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
     * Verifies if the riposte can be applied if an entity has the Stun effect.
     * @param source The player attempting the riposte.
     * @param target The target entity.
     * @return True if the riposte can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Entity source, LivingEntity target) {
        return target.hasEffect(ModMobEffects.STUN);
    }

    @Override
    public boolean hasAdditionalModifiers() {
        return false;
    }

    @Override
    public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
        return 0;
    }

    @Override
    public void applyOnHitFunction(LivingIncomingDamageEvent event) {
        if (event.getEntity().hasEffect(ModMobEffects.STUN)) {
            event.getEntity().removeEffect(ModMobEffects.STUN);
        }
    }

    @Override
    public float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
