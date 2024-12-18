package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

// UTILITY METHODS RELATED TO ENTITIES AND PLAYERS
public class EntityUtils {

    // Checkers

    public static boolean isBeingTargeted(Player target, LivingEntity source) {
        if (source instanceof Mob mob) {
            // Returns if an entity is the target or null
            return mob.getTarget() != null && mob.getTarget().is(target);
        }
        return false;
    }

    public static boolean isAwareOf(Player player, LivingEntity entity) {
        if (entity instanceof Mob mob) {
            return mob.getLastAttacker() == player || mob.getLastHurtMob() == player;
        }
        return false;
    }

    public static boolean isBehind(Player source, LivingEntity target) {
        return source.getDirection() == target.getDirection();
    }

    // Modifiers

    public static void stunTarget(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(ModMobEffects.STUN, 60, 1, false, false));
    }

}
