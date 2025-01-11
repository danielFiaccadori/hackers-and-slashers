package net.dndats.hackersandslashers.api.combat.mechanics.stealth;

import net.dndats.hackersandslashers.api.combat.mechanics.ai.SearchLostPlayerGoal;
import net.dndats.hackersandslashers.common.setup.ModPlayerData;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

// MANAGES STEALTH LOGICS
public class Stealth {

    /**
     * This method represents the stealth mechanic behavior
     *
     * @param event: the event responsible by changing the behavior of aware mobs
     */

    public static void stealthBehavior(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob && event.getNewAboutToBeSetTarget() instanceof Player player) {

            int alertPoints = calculateAlertPoints(player);
            int alertLevel = EntityHelper.getAlertLevel(mob);

            if (alertLevel > 50) {
                makeMobSearch(mob, player);
            }

            if (EntityHelper.canSee(player, mob)) {
                if (alertLevel < 100) {
                    int newAlertLevel = Math.min(100, Math.max(0, alertLevel + alertPoints));
                    EntityHelper.setAlertLevel(mob, newAlertLevel);
                }
            } else {
                EntityHelper.setAlert(mob, false);
            }

            if (alertLevel < 100) {
                event.setCanceled(true);
            }

        }
    }

    /**
     * These methods are utility methods to be used in the stealth behavior method.
     *
     * @param mob: the mob in context
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

    private static int calculateAlertPoints(Player player) {
        int points = 25 + PlayerHelper.getArmorLevel(player);
        if (PlayerHelper.isOnBush(player)) points -= 25;
        if (PlayerHelper.isAtDarkPlace(player)) points -= 20;
        if (PlayerHelper.isMoving(player)) points += 5;
        return points;
    }

    private static void mobAlertSetter(Player player) {
        if (player == null) return;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(64)).forEach(mob -> {
                    if (mob.getPersistentData().contains("is_alert")) {
                        EntityHelper.setAlert(mob, EntityHelper.canSee(player, mob));
                    }
                });

    }

    /**
     * This is a method that changes the visibility level data attachment of the player
     * The data attachment is purely used for a visual feedback of the detection overlay, although it needs a server side behavior too
     *
     * @param player: the player in context
     */

    public static void updatePlayerVisibility(Player player) {
        if (player == null) return;
        mobAlertSetter(player);
        var playerData = player.getData(ModPlayerData.VISIBILITY_LEVEL);
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        Mob mostAlertMob = player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(64))
                .stream().max((mob1, mob2) -> {
                    int alertMob1 = EntityHelper.getAlertLevel(mob1);
                    int alertMob2 = EntityHelper.getAlertLevel(mob2);
                    return Integer.compare(alertMob1, alertMob2);
                }).orElse(null);
        if (mostAlertMob != null) {
            player.sendSystemMessage(Component.literal("The most alert mob is " + EntityHelper.getAlertLevel(mostAlertMob) + "% alert"));

            int alertLevel = EntityHelper.getAlertLevel(mostAlertMob);
            int newVisibilityLevel = calculateVisibilityLevel(alertLevel);

            if (PlayerHelper.getVisibilityLevel(player) != newVisibilityLevel) {
                playerData.setVisibilityLevel(newVisibilityLevel);
                playerData.syncData(player);
            }
        }
    }

    private static int calculateVisibilityLevel(int alertLevel) {
        if (alertLevel >= 100) return 100;
        if (alertLevel > 80) return 80;
        if (alertLevel > 60) return 60;
        if (alertLevel > 40) return 40;
        if (alertLevel > 20) return 20;
        return 0;
    }

}