package net.dndats.hackersandslashers.api.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.Nullable;

public interface ICriticalLogic {

    /**
      This interface holds all the logic of a critical hit.
      New critical logics should implement this interface.
    */

    boolean canBeApplied(Entity source, LivingEntity target);
    boolean hasAdditionalModifiers();
    float getAdditionalModifiers(LivingIncomingDamageEvent event);
    void applyOnHitFunction(LivingIncomingDamageEvent event);
    float getDamageMultiplier();

}
