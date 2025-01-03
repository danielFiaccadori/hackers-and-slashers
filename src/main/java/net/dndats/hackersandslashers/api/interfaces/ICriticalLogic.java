package net.dndats.hackersandslashers.api.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface ICriticalLogic {

    /**
      This interface holds all the logic of a critical hit.
      New critical types implements this interface.
    */

    boolean canBeApplied(Entity source, LivingEntity target);
    float getDamageMultiplier();

}
