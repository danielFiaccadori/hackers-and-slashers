package net.dndats.hackersandslashers.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

// UTILITY METHODS RELATED TO ENTITIES AND PLAYERS
public class EntityUtils {

    public static boolean isBeingTargeted(Player target, Entity source) {
        if (source instanceof Mob mob) {
            // Returns if an entity is the target or null
            return mob.getTarget() != null && mob.getTarget().is(target);
        }
        return false;
    }

    public static boolean isAwareOf(Player player, Entity entity) {
        if (entity instanceof Mob mob) {
            return mob.getLastAttacker() != player && mob.getLastHurtMob() != player;
        }
        return false;
    }

    public static boolean isInCombat(Entity entity) {
        if (entity instanceof Mob mob) {
            return mob.getTarget() != null;
        }
        return false;
    }

    public static boolean isBehind(Player source, Entity target) {
        return source.getDirection() == target.getDirection();
    }

}
