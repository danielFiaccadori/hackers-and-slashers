package net.dndats.hackersandslashers.api.combat.mechanics.stealth;

import net.dndats.hackersandslashers.api.combat.mechanics.ai.SearchLostPlayerGoal;
import net.dndats.hackersandslashers.common.data.MobDetectabilityData;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Objects;

public class Stealth {

    /**
     * This method represents the stealth mechanic behavior
     *
     * @param event: the event responsible by changing the behavior of aware mobs
     */

    public static void stealthBehavior(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob && event.getNewAboutToBeSetTarget() instanceof Player player) {

            /*

                                                   Welcome to the
                                             --{ STEALTH'O METER :D }--

            */

            int alertPoints = calculateAlertPoints(player, mob);
            int alertLevel = EntityHelper.getAlertLevel(mob);

            EntityHelper.setCurrentTargetId(mob, player);

            if (EntityHelper.isAwareOf(player, mob)) {
                makeMobSearch(mob, player);
            }

            if (alertLevel == 0) {
                if (!EntityHelper.isAwareOf(player, mob)) {
                    mob.setTarget(null);
                }
            }

            if (EntityHelper.canSee(player, mob)) {
                if (alertLevel < 100) {
                    int newAlertLevel = Math.min(100, Math.max(0, alertLevel + alertPoints));
                    EntityHelper.setAlertLevel(mob, newAlertLevel);
                    EntityHelper.setAlert(mob, true);
                }
            } else {
                if (EntityHelper.getAlertLevel(mob) == 0) {
                    EntityHelper.resetCurrentTargetId(mob);
                    player.sendSystemMessage(Component.literal("Target from " + mob.getDisplayName() + " reseted"));
                }
                EntityHelper.setAlert(mob, false);
            }

            if (alertLevel < 100) {
                event.setCanceled(true);
            }

        }
    }

    /**
     * This method calculates the probability of the player being targeted based on its environment.
     * The amount is calculated by points, where the default base punctuation is of 10.
     *
     * @param player : the player in context
     * @param mob: the mob in context
     * @return the amount of points, or "probability" of the mob being seen
     */

    private static int calculateAlertPoints(Player player, Mob mob) {
        int points = 10 + PlayerHelper.getArmorLevel(player) + PlayerHelper.lightLevel(player);
        if (player.level().isDay()) points += DAY_WEIGHT;
        if (player.level().isRaining() || player.level().isThundering()) points -= RAINING_WEIGHT;
        if (player.isSprinting()) points += SPRINTING_WEIGHT;
        if (player.isCrouching()) points -= CROUCHING_WEIGHT;
        if (PlayerHelper.isOnBush(player)) points -= ON_BUSH_WEIGHT;
        if (PlayerHelper.isMoving(player)) points += MOVING_WEIGHT;
        if (PlayerHelper.isPlayerBehind(mob, player)) points -= IS_BEHIND_WEIGHT;
        if (EntityHelper.isAwareOf(player, mob)) points += LAST_ATTACKER_WEIGHT;
        if (!isBeingSeen(player)) points -= BEING_SEEN_WEIGHT;
        return points;
    }

    // Environment weights

    private static final int LAST_ATTACKER_WEIGHT = 50;
    private static final int CROUCHING_WEIGHT = 30;
    private static final int DAY_WEIGHT = 30;
    private static final int ON_BUSH_WEIGHT = 20;
    private static final int RAINING_WEIGHT = 10;
    private static final int IS_BEHIND_WEIGHT = 10;
    private static final int SPRINTING_WEIGHT = 10;
    private static final int BEING_SEEN_WEIGHT = 10;
    private static final int MOVING_WEIGHT = 5;

    /**
     * This is a method that changes the visibility level data attachment of the player
     * The data attachment is purely used for a visual feedback of the detection overlay, although it needs a server side behavior too
     *
     * @param player: the player in context
     */

    public static void updatePlayerVisibility(Player player) {
        if (player == null) return;
        mobAlertSetter(player);
        var playerData = player.getData(ModData.VISIBILITY_LEVEL);
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        Mob mostAlertMob = player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(32))
                .stream()
                .filter(mob -> {
                    MobDetectabilityData data = mob.getData(ModData.MOB_DETECTABILITY);
                    return !Objects.equals(data.getCurrentTargetUUID(), "")
                            && data.getCurrentTargetUUID().equals(player.getUUID().toString());
                })
                .max((mob1, mob2) -> {
                    int alertMob1 = EntityHelper.getAlertLevel(mob1);
                    int alertMob2 = EntityHelper.getAlertLevel(mob2);
                    return Integer.compare(alertMob1, alertMob2);
                }).orElse(null);
        if (mostAlertMob != null) {
            int alertLevel = EntityHelper.getAlertLevel(mostAlertMob);
            int newVisibilityLevel = calculateVisibilityLevel(alertLevel);
            player.sendSystemMessage(Component.literal("An entity has " + EntityHelper.getAlertLevel(mostAlertMob) + "% of alert level"));

            if (PlayerHelper.getVisibilityLevel(player) != newVisibilityLevel) {
                playerData.setVisibilityLevel(newVisibilityLevel);
                playerData.syncData(player);
            }

        } else {
            if (PlayerHelper.getVisibilityLevel(player) != 0) {
                player.sendSystemMessage(Component.literal("Visibility set to 0."));
                playerData.setVisibilityLevel(0);
                playerData.syncData(player);
            }
        }
    }

    private static int calculateVisibilityLevel(int alertLevel) {
        if (alertLevel >= 100) return 100;
        if (alertLevel > 90) return 90;
        if (alertLevel > 80) return 80;
        if (alertLevel > 70) return 70;
        if (alertLevel > 60) return 60;
        if (alertLevel > 50) return 50;
        if (alertLevel > 40) return 40;
        if (alertLevel > 30) return 30;
        if (alertLevel > 20) return 20;
        if (alertLevel > 10) return 10;
        return 0;
    }

    /**
     * This method manages the continuity of alert level to aggressive and passive mobs
     *
     * @param event: the event responsible by managing the alert state
     */


    public static void mobAlert(EntityTickEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            if (!EntityHelper.isAlert(mob)) {
                if (EntityHelper.getAlertLevel(mob) > 0) {
                    EntityHelper.setAlertLevel(mob, EntityHelper.getAlertLevel(mob) - 1);
                }
            }
        } else {
            if (event.getEntity() instanceof AgeableMob ageableMob) {
                if (ageableMob.isPanicking()) {
                    EntityHelper.setAlertLevel(ageableMob, 100);
                } else {
                    EntityHelper.setAlertLevel(ageableMob, 0);
                }
            }
        }
    }

    /**
     * These methods are utility methods to be used in the stealth behavior method.
     *
     * @param mob:    the mob in context
     * @param player: the player in context
     */

    private static void makeMobSearch(Mob mob, Player player) {
        if (mob.goalSelector.getAvailableGoals().stream()
                .noneMatch(goal -> goal.getGoal() instanceof SearchLostPlayerGoal)
                && !(mob instanceof Warden)) {
            mob.playAmbientSound();
            mob.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
            mob.goalSelector.addGoal(5, new SearchLostPlayerGoal(mob));
        }
    }

    private static boolean isBeingSeen(Player player) {
        if (player == null) return false;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        for (Mob mob : player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(32))) {
            if (EntityHelper.canSee(player, mob)) {
                return true;
            }
        }
        return false;
    }

    private static void mobAlertSetter(Player player) {
        if (player == null) return;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(32)).forEach(mob -> {
            if (mob.getPersistentData().contains("is_alert")) {
                EntityHelper.setAlert(mob, EntityHelper.canSee(player, mob));
            }
        });

    }

}