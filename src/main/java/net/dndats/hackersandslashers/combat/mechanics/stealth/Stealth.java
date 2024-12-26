package net.dndats.hackersandslashers.combat.mechanics.stealth;

import net.dndats.hackersandslashers.TickScheduler;
import net.dndats.hackersandslashers.network.packets.PlayerDetectionStatePacket;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.network.PacketDistributor;

// MANAGES STEALTH LOGICS
public class Stealth {

    private static final int DETECTION_WAIT_TIME = 60;

    /**
     * This class represents the stealth mechanic behavior
     *
     * @param event: the event responsible by changing the behavior of aware mobs
     */

    public static void mobsIgnoreStealthyTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob && event.getNewAboutToBeSetTarget() instanceof Player player) {

            if (EntityUtils.getMobAlertLevel(mob) == 50) {
                event.setNewAboutToBeSetTarget(null);
            }

            if (!EntityUtils.hasAlertTag(mob)) {
                // If the mob is not alert, enters here
                if (isAtObfuscatedPlace(player) && (!isStealthy(player)) && !EntityUtils.isAwareOf(player, mob)) {
                    // If the target (player) is at a place of hard vision but not stealthy and not in combat with the mob, then enters here
                    EntityUtils.addAlertTag(mob, 50);
                    TickScheduler.schedule(() -> {
                        // After some seconds, enters here
                        if (isStealthy(player) && !EntityUtils.isAwareOf(player, mob)) {
                            EntityUtils.removeAlertTag(mob);
                            event.setNewAboutToBeSetTarget(null);
                        } else {
                            EntityUtils.addAlertTag(mob, 100);
                            mob.setTarget(player);
                        }
                    }, DETECTION_WAIT_TIME);
                } else {
                    if (isStealthy(player) && !EntityUtils.isAwareOf(player, mob)) {
                        EntityUtils.removeAlertTag(mob);
                        event.setNewAboutToBeSetTarget(null);
                    } else {
                        EntityUtils.addAlertTag(mob, 100);
                        mob.setTarget(player);
                    }
                }

            }

        }
    }

    /**
     * This is a complex method who changes the visibility level data attachment of the player (I spent a lot of time in it ;-;)
     * The data attachment is purely used for a visual feedback of the detection overlay, although it needs a server side behavior too
     *
     * @param player: the player in context
     */

    public static void detectBeingTargeted(Player player) {
        if (player == null) return;
        if (mobAlertChecker(player)) {
            if (mobTargetChecker(player)) {
                if (PlayerUtils.getVisibilityLevel(player) != 100) {
                    PacketDistributor.sendToServer(new PlayerDetectionStatePacket(100));
                }
            } else {
                if (PlayerUtils.getVisibilityLevel(player) != 50) {
                    PacketDistributor.sendToServer(new PlayerDetectionStatePacket(50));
                }
            }
        } else {
            if (PlayerUtils.getVisibilityLevel(player) != 0) {
                PacketDistributor.sendToServer(new PlayerDetectionStatePacket(0));
            }
        }
    }

    /**
     * Here, both methods above are used for verifying if some mob in a determined area (default of 64) is targeting the player or is alert
     * It basically iterates in all mobs around, if the check is true, it returns a boolean value
     *
     * @param player: the player in context
     * @return: after tracking, returns the waited boolean value
     */

    private static boolean mobTargetChecker(Player player) {
        if (player == null) return false;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        return player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(64))
                .stream()
                .anyMatch(mob -> mob.getTarget() == player);

    }

    private static boolean mobAlertChecker(Player player) {
        if (player == null) return false;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        return player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(64))
                .stream()
                .anyMatch(EntityUtils::hasAlertTag);
    }

    private static boolean isAtObfuscatedPlace(Player player) {
        return PlayerUtils.isAtDarkPlace(player);
    }

    private static boolean isStealthy(Player player) {
        return (PlayerUtils.isOnBush(player) || PlayerUtils.isAtDarkPlace(player)) &&
                (player.isCrouching() || player.isInvisible());
    }

}