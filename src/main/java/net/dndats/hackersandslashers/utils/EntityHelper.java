package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.setup.ModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

// UTILITY METHODS RELATED TO ENTITIES AND PLAYERS
public class EntityHelper {

    public static boolean isBeingTargeted(Player target, LivingEntity source) {
        if (source instanceof Mob mob) {
            // Returns if an entity is the target or null
            return mob.getTarget() != null && mob.getTarget().is(target);
        }
        return false;
    }

//    public static void resetMobTarget(EntityTickEvent event) {
//        if (event.getEntity() instanceof Mob mob) {
//            CompoundTag nbt = mob.getPersistentData();
//            if (nbt.getBoolean("is_alert") && mob.getTarget() == null) {
//                nbt.remove("is_alert");
//            }
//        }
//    }

    private static final String LOST_HEALTH_PERCENT = "lost_health_percent";

    public static int getMobLostHealth(LivingEntity entity) {
        CompoundTag nbt = entity.getPersistentData();
        return nbt.getInt(LOST_HEALTH_PERCENT);
    }

    public static void updateLostHealth(LivingEntity entity) {
        CompoundTag nbt = entity.getPersistentData();
        float maxHealth = entity.getMaxHealth();
        float currentHealth = entity.getHealth();
        int lostHealthPercent = (int) ((1 - (currentHealth / maxHealth)) * 100);
        nbt.putInt(LOST_HEALTH_PERCENT, lostHealthPercent);
        HackersAndSlashers.LOGGER.info("{}% of lost health", nbt.getInt(LOST_HEALTH_PERCENT));
    }

    private static final String IS_ALERT = "is_alert";
    private static final String ALERT_LEVEL = "alert_level";
    private static final String CURRENT_TARGET_UUID = "current_target_uuid";

    public static void setCurrentTargetId(Mob mob, Player player) {
        CompoundTag nbt = mob.getPersistentData();
        nbt.putUUID(CURRENT_TARGET_UUID, player.getUUID());
    }

    public static UUID getCurrentTargetId(Mob mob) {
        CompoundTag nbt = mob.getPersistentData();
        return nbt.getUUID(CURRENT_TARGET_UUID);
    }

    public static boolean isAlert(Mob mob) {
        CompoundTag nbt = mob.getPersistentData();
        return nbt.getBoolean(IS_ALERT);
    }

    public static void setAlert(Mob mob, boolean is) {
        CompoundTag nbt = mob.getPersistentData();
        nbt.putBoolean(IS_ALERT, is);
    }

    public static int getAlertLevel(Mob mob) {
        CompoundTag nbt = mob.getPersistentData();
        return nbt.getInt(ALERT_LEVEL);
    }

    public static void setAlertLevel(Mob mob, int alertLevel) {
        CompoundTag nbt = mob.getPersistentData();
        nbt.putInt(ALERT_LEVEL, alertLevel);
    }

    public static void removeAlertTags(Mob mob) {
        CompoundTag nbt = mob.getPersistentData();
        nbt.remove(ALERT_LEVEL);
        nbt.remove(IS_ALERT);
    }

    public static int getDistanceBetweenEntities(LivingEntity entity1, LivingEntity entity2) {
        double x1 = entity1.getX();
        double y1 = entity1.getY();
        double z1 = entity1.getZ();

        double x2 = entity2.getX();
        double y2 = entity2.getY();
        double z2 = entity2.getZ();

        return (int) Math.sqrt(
                Math.pow(x2 - x1, 2) +
                        Math.pow(y2 - y1, 2) +
                        Math.pow(z2 - z1, 2)
        );
    }

    public static boolean canSee(Player player, LivingEntity entity) {
        return entity.hasLineOfSight(player);
    }

    public static boolean isAwareOf(Player player, LivingEntity entity) {
        return entity.getLastAttacker() == player;
    }

    // Modifiers

    public static void stunTarget(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(ModMobEffects.STUN, 60, 1, false, false));
    }

}
