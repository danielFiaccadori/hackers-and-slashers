package net.dndats.hackersandslashers.combat.mechanics.stealth;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

// MANAGES STEALTH LOGICS
public class Stealth {

    // Logic for stealthiness from Players : Mobs
    public static void mobsIgnoreStealthyTarget(LivingChangeTargetEvent event) {
        if (event.getNewAboutToBeSetTarget() instanceof Player player &&
            event.getEntity() instanceof Mob mob) {
            if (isStealthy(player) && EntityUtils.isInCombat(mob)) {
                // If player is on dark place or on bush and crouching, cancels target
                HackersAndSlashers.LOGGER.info("{} is hidden", player.getDisplayName());
                event.setNewAboutToBeSetTarget(null);
            }
        }
    }

    protected static boolean isStealthy(Player player) {
        return (PlayerUtils.isOnBush(player) || PlayerUtils.isOnDarkPlace(player)) && player.isCrouching();
    }

}