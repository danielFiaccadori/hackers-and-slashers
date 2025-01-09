package net.dndats.hackersandslashers.api.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public interface ICriticalLogic {

    /**
      This interface holds all the logic of a critical hit.
      New critical logics should implement this interface.
    */

    // verifies if the critical can be applied
    boolean canBeApplied(Entity source, LivingEntity target);
    // Adds additional modifiers (ex: backstab attack speed scaling)
    float getAdditionalModifiers(LivingIncomingDamageEvent event);
    // Apply a function when the critical is dealt
    void applyOnHitFunction(LivingIncomingDamageEvent event);
    // Gets the base damage multiplier without additional modifiers
    float getDamageMultiplier();

}
