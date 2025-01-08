package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// UTILITY METHODS RELATED TO COMBAT
public class CombatHelper {

    // Modifiers

    public static float dealCriticalDamage(float multiplier, LivingIncomingDamageEvent event) {
        try {
            float finalAmount = event.getOriginalAmount() * multiplier;
            event.setAmount(finalAmount);
            return finalAmount;
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical damage: {}", e.getMessage());
        }
        return 0;
    }

    public static void stunAttackingEntity(LivingIncomingDamageEvent event) {
        try {
            if (event.getSource().getEntity() instanceof LivingEntity target && event.getEntity() instanceof Player player) {
                if (target.getHealth() < player.getHealth()) {
                    if (PlayerHelper.isBlocking(player)) {
                        EntityHelper.stunTarget(target);
                    }
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to stun a target: {}", e.getMessage());

        }
    }

    public static void spawnCombatParticles(LivingIncomingDamageEvent event, Boolean isCritical) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!isCritical) {
            if (event.getSource().getWeaponItem().getItem() instanceof SwordItem) {
                VisualEffects.spawnAttackParticles(
                        target.level(),
                        target.getX(),
                        target.getY() + 0.25 + target.getBbHeight() / 2,
                        target.getZ()
                );
            } else if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
                VisualEffects.spawnAttackParticles(
                        projectile.level(),
                        projectile.getX(),
                        projectile.getY(),
                        projectile.getZ()
                );
            }
        } else {
            if (event.getSource().getWeaponItem().getItem() instanceof SwordItem) {
                VisualEffects.spawnAttackCritParticles(
                        target.level(),
                        target.getX(),
                        target.getY() + 0.25 + target.getBbHeight() / 2,
                        target.getZ()
                );
            } else if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
                VisualEffects.spawnAttackCritParticles(
                        projectile.level(),
                        projectile.getX(),
                        projectile.getY(),
                        projectile.getZ()
                );
            }
        }
    }

}
