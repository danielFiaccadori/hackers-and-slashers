package net.dndats.hackersandslashers.combat.critical;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// BASE CRITICAL TYPE IMPLEMENTATION
public abstract class CriticalAttack implements CriticalHit {

    private final String NAME;
    private final Float DAMAGE_MULTIPLIER;

    protected CriticalAttack(String name, Float damageMultiplier) {
        NAME = name;
        DAMAGE_MULTIPLIER = damageMultiplier;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

    @Override
    public abstract void applyCritical(LivingIncomingDamageEvent event);

}
