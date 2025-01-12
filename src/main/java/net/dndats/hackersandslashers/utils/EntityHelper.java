package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.setup.ModData;
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

    public static void setCurrentTargetId(Mob mob, Player player) {
        mob.getData(ModData.MOB_DETECTABILITY).setCurrentTargetUUID(player.getUUID().toString());
    }

    public static void resetCurrentTargetId(Mob mob) {
        mob.getData(ModData.MOB_DETECTABILITY).setCurrentTargetUUID("");
    }

    public static String getCurrentTargetId(Mob mob) {
        return mob.getData(ModData.MOB_DETECTABILITY).getCurrentTargetUUID();
    }

    public static void setAlert(Mob mob, boolean is) {
        mob.getData(ModData.MOB_DETECTABILITY).setAlert(is);
    }

    public static boolean isAlert(Mob mob) {
        return mob.getData(ModData.MOB_DETECTABILITY).isAlert();
    }

    public static void setAlertLevel(Mob mob, int alertLevel) {
        mob.getData(ModData.MOB_DETECTABILITY).setAlertLevel(alertLevel);
    }

    public static int getAlertLevel(Mob mob) {
        return mob.getData(ModData.MOB_DETECTABILITY).getAlertLevel();
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
