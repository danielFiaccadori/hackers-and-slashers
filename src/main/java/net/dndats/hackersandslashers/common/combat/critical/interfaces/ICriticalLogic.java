package net.dndats.hackersandslashers.common.combat.critical.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface ICriticalLogic {

    /*
      This interface holds all the logic of a critical hit.
      New critical types NEED to implement this interface for organization purposes.
    */

    boolean canBeApplied(Entity source, LivingEntity target);
    float getDamageMultiplier();

}
