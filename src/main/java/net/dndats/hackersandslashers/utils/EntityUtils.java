package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

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

    public static void resetMobTarget(EntityTickEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            CompoundTag nbt = mob.getPersistentData();
            if (nbt.getBoolean("is_alert") && mob.getTarget() == null) {
                nbt.remove("is_alert");
            }
        }
    }

    private static final String ALERT_TAG = "alert_level";

    public static boolean hasAlertTag(Mob mob) {
        CompoundTag nbt = mob.getPersistentData();
        return nbt.contains(ALERT_TAG);
    }

    public static int getMobAlertLevel(Mob mob) {
        CompoundTag nbt = mob.getPersistentData();
        return nbt.getInt("alert_level");
    }

    public static void addAlertTag(Mob mob, int alertLevel) {
        CompoundTag nbt = mob.getPersistentData();
        nbt.putInt(ALERT_TAG, alertLevel);
    }

    public static void removeAlertTag(Mob mob) {
        CompoundTag nbt = mob.getPersistentData();
        nbt.remove(ALERT_TAG);
    }

    public static boolean isAwareOf(Player player, LivingEntity entity) {
        return entity.getLastAttacker() != player;
    }

    public static boolean isBehind(Player source, LivingEntity target) {
        return source.getDirection() == target.getDirection();
    }

    // Modifiers

    public static void stunTarget(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(ModMobEffects.STUN, 60, 1, false, false));
    }

}
