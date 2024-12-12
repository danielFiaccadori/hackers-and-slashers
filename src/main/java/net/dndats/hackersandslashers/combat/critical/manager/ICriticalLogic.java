package net.dndats.hackersandslashers.combat.critical.manager;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public interface ICriticalLogic {

    /*
      This interface holds all the logic of a critical hit.
      New critical types NEED to implement this interface for organization purposes.
    */

    void apply(LivingIncomingDamageEvent event);
    boolean canBeApplied(Player source, LivingEntity target);

}
