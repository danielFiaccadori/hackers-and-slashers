package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.particles.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import javax.annotation.Nullable;

// UTILITY METHODS RELATED TO COMBAT
public class CombatUtils {

    // Modifiers

    public static void dealCriticalDamage(float multiplier, LivingIncomingDamageEvent event) {
        try {
            event.setAmount(event.getOriginalAmount() * multiplier);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical damage: {}", e.getMessage());
        }
    }

    public static void stunAttackingEntity(LivingIncomingDamageEvent event) {
        try {
            if (event.getSource().getEntity() instanceof LivingEntity target && event.getEntity() instanceof Player player) {
                if (target.getHealth() < player.getHealth()) {
                    if (PlayerUtils.isBlocking(player)) {
                        EntityUtils.stunTarget(target);
                    }
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to stun a target: {}", e.getMessage());

        }

    }
}
