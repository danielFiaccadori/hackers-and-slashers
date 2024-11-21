package net.dndats.hackersandslashers.combat.critical;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// BASE INTERFACE FOR CRITICAL HITS
public interface CriticalHit {

    void applyCritical(LivingIncomingDamageEvent event);

    String getName();
    Float getDamageMultiplier();

}
