package net.dndats.hackersandslashers.combat.mechanics.stealth;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.TickScheduler;
import net.dndats.hackersandslashers.network.packets.PlayerDetectionStatePacket;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static net.dndats.hackersandslashers.common.ModData.IS_ALERT;
import static net.dndats.hackersandslashers.common.ModData.IS_HIDDEN;

// MANAGES STEALTH LOGICS

public class Stealth {

    private static final int DETECTION_WAIT_TIME = 90;
    public static void mobsIgnoreStealthyTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob && event.getNewAboutToBeSetTarget() instanceof Player player) {

            if (!mob.getData(IS_ALERT)) {
                if (isAtObfuscatedPlace(player) && (!isStealthy(player)) && !EntityUtils.isAwareOf(player, mob)) {
                    event.setNewAboutToBeSetTarget(null);
                    mob.setData(IS_ALERT, false);
                    TickScheduler.schedule(() -> {
                        if (isStealthy(player) && !EntityUtils.isAwareOf(player, mob)) {
                            HackersAndSlashers.LOGGER.info("{}: hmm, maybe i'm crazy.", mob.getDisplayName().getString());
                            mob.setData(IS_ALERT, false);
                            event.setNewAboutToBeSetTarget(null);
                        } else {
                            HackersAndSlashers.LOGGER.info("{}: i see you!", mob.getDisplayName().getString());
                            mob.setData(IS_ALERT, true);
                            mob.setTarget(player);
                        }
                    }, DETECTION_WAIT_TIME);
                } else {
                    if (isStealthy(player) && !EntityUtils.isAwareOf(player, mob)) {
                        mob.setData(IS_ALERT, false);
                        event.setNewAboutToBeSetTarget(null);
                    } else {
                        mob.setData(IS_ALERT, true);
                        mob.setTarget(player);
                    }
                }

            }

        }
    }

    public static void detectBeingTargeted(Player player) {
        if (player == null) return;
        if (mobTargetChecker(player)) {
            if (!player.getData(IS_HIDDEN)) {
                PacketDistributor.sendToServer(new PlayerDetectionStatePacket(true));
                player.sendSystemMessage(Component.literal("Is hidden: " + player.getData(IS_HIDDEN)));
            }
        } else {
            if (player.getData(IS_HIDDEN)) {
                PacketDistributor.sendToServer(new PlayerDetectionStatePacket(false));
                player.sendSystemMessage(Component.literal("Is hidden: " + player.getData(IS_HIDDEN)));
            }
        }
    }

    public static boolean detectAlertMobs(Player player) {
        if (player == null) return false;
        return mobAlertChecker(player);
    }

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
                .anyMatch(mob -> mob.getData(IS_ALERT));
    }

    private static boolean isAtObfuscatedPlace(Player player) {
        return PlayerUtils.isAtDarkPlace(player);
    }

    private static boolean isStealthy(Player player) {
        return (PlayerUtils.isOnBush(player) || PlayerUtils.isAtDarkPlace(player)) &&
                (player.isCrouching() || player.isInvisible());
    }

}