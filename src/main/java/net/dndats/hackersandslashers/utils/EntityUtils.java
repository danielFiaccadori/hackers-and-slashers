package net.dndats.hackersandslashers.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

// UTILITY METHODS RELATED TO ENTITIES
public class EntityUtils {

    public static boolean isBeingTargeted(LivingEntity source, Player target) {
        if (source instanceof Mob mob) {
            // Returns if an entity is the target or null
            return mob.getTarget() != null && mob.getTarget().is(target);
        }
        return false;
    }

}
