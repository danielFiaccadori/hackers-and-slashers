package net.dndats.hackersandslashers.api.combat.mechanics.stealth;

import net.dndats.hackersandslashers.api.combat.mechanics.stealth.ai.SearchLostPlayerGoal;
import net.dndats.hackersandslashers.utils.TickScheduler;
import net.dndats.hackersandslashers.common.setup.ModPlayerData;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

// MANAGES STEALTH LOGICS
public class Stealth {

    private static final int DETECTION_WAIT_TIME = 60; // TICKS

    /**
     * This method represents the stealth mechanic behavior
     *
     * @param event: the event responsible by changing the behavior of aware mobs
     */

    public static void stealthBehavior(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob && event.getNewAboutToBeSetTarget() instanceof Player player) {

            if (EntityHelper.getMobAlertLevel(mob) == 50) {
                event.setNewAboutToBeSetTarget(null);
            }

            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(goal -> goal.getGoal() instanceof SearchLostPlayerGoal)
                    && !(mob instanceof Warden)) {
                mob.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
                player.sendSystemMessage(Component.literal("I saw something..."));
                mob.goalSelector.addGoal(5, new SearchLostPlayerGoal(mob));
            }

            if (!EntityHelper.hasAlertTag(mob)) {
                // If the mob is not alert, enters here
                if (PlayerHelper.isAtDarkPlace(player) && (!isStealthy(player)) && !EntityHelper.isAwareOf(player, mob)) {
                    // If the target (player) is at a place of difficult vision but not stealthy and not in combat with the mob, then enters here
                    EntityHelper.addAlertTag(mob, 50);
                    TickScheduler.schedule(() -> {
                        // After some seconds, enters here
                        if (isStealthy(player) && !EntityHelper.isAwareOf(player, mob)) {
                            EntityHelper.removeAlertTag(mob);
                            event.setNewAboutToBeSetTarget(null);
                        } else {
                            EntityHelper.addAlertTag(mob, 100);
                            mob.setTarget(player);
                            player.sendSystemMessage(Component.literal("Gotcha!"));
                        }
                    }, DETECTION_WAIT_TIME - Math.max(player.getArmorValue(), 0));
                } else {
                    if (isStealthy(player) && !EntityHelper.isAwareOf(player, mob)) {
                        EntityHelper.removeAlertTag(mob);
                        event.setNewAboutToBeSetTarget(null);
                    } else {
                        EntityHelper.addAlertTag(mob, 100);
                        mob.setTarget(player);
                        player.sendSystemMessage(Component.literal("Gotcha! With no excitation!"));
                    }
                }
            }

        }
    }

    /**
     * This is a method that changes the visibility level data attachment of the player
     * The data attachment is purely used for a visual feedback of the detection overlay, although it needs a server side behavior too
     *
     * @param player: the player in context
     */

    public static void detectBeingTargeted(Player player) {
        if (player == null) return;
        var playerData = player.getData(ModPlayerData.VISIBILITY_LEVEL);
        if (mobAlertChecker(player) || mobLastAttackerChecker(player) || mobSightChecker(player)) {
            if (mobTargetChecker(player) || mobLastAttackerChecker(player)) {
                if (PlayerHelper.getVisibilityLevel(player) != 100) {
                    playerData.setVisibilityLevel(100);
                    playerData.syncData(player);
                }
            } else if (mobSightChecker(player)){
                if (PlayerHelper.getVisibilityLevel(player) != 50) {
                    playerData.setVisibilityLevel(50);
                    playerData.syncData(player);
                }
            } else {
                if (PlayerHelper.getVisibilityLevel(player) != 50) {
                    playerData.setVisibilityLevel(50);
                    playerData.syncData(player);
                }
            }
        } else {
            if (PlayerHelper.getVisibilityLevel(player) != 0) {
                playerData.setVisibilityLevel(0);
                playerData.syncData(player);
            }
        }
    }

    /**
     * Here, both methods above are used for verifying if some mob in a determined area (default of 64) is targeting the player or is alert.
     * It basically iterates in all mobs around, if the check is true, it returns a boolean value
     *
     * @param player: the player in context
     * @return after tracking, the waited boolean value
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
                .anyMatch(EntityHelper::hasAlertTag);
    }

    private static boolean mobLastAttackerChecker(Player player) {
        if (player == null) return false;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        return player.level().getEntitiesOfClass(LivingEntity.class, new AABB(surroundings, surroundings).inflate(64))
                .stream()
                .anyMatch(mob -> mob.getLastAttacker() == player && mob.getHealth() > 0);
    }

    private static boolean mobSightChecker(Player player) {
        if (player == null) return false;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        return player.level().getEntitiesOfClass(LivingEntity.class, new AABB(surroundings, surroundings).inflate(64))
                .stream()
                .anyMatch(entity -> {
                    if (!PlayerHelper.isOnBush(player) || !PlayerHelper.isAtDarkPlace(player)) {
                        return entity.hasLineOfSight(player) && entity != player;
                    }
                    return false;
                });
    }

    private static boolean isStealthy(Player player) {
        return (PlayerHelper.isOnBush(player) || PlayerHelper.isAtDarkPlace(player)) &&
                (player.isCrouching() || player.isInvisible());
    }
}