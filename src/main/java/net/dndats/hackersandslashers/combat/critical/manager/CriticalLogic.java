package net.dndats.hackersandslashers.combat.critical.manager;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public interface CriticalLogic {

    void apply(LivingIncomingDamageEvent event);
    boolean canBeApplied(Player source, Entity target);

}
