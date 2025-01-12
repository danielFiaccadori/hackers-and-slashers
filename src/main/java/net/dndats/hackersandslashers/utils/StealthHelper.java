package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.api.combat.mechanics.ai.SearchLostPlayerGoal;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class StealthHelper {

    public static void makeMobSearch(Mob mob, Player player) {
        if (mob.goalSelector.getAvailableGoals().stream()
                .noneMatch(goal -> goal.getGoal() instanceof SearchLostPlayerGoal)
                && !(mob instanceof Warden)) {
            mob.playAmbientSound();
            mob.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
            mob.goalSelector.addGoal(5, new SearchLostPlayerGoal(mob));
        }
    }

    public static boolean isBeingSeen(Player player) {
        if (player == null) return false;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        for (Mob mob : player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(32))) {
            if (EntityHelper.canSee(player, mob)) {
                return true;
            }
        }
        return false;
    }

    public static void mobAlertSetter(Player player) {
        if (player == null) return;
        final Vec3 surroundings = new Vec3(player.getX(), player.getY(), player.getZ());
        player.level().getEntitiesOfClass(Mob.class, new AABB(surroundings, surroundings).inflate(32)).forEach(mob -> {
            if (mob.getPersistentData().contains("is_alert")) {
                EntityHelper.setAlert(mob, EntityHelper.canSee(player, mob));
            }
        });

    }

}
