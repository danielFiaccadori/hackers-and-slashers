package net.dndats.hackersandslashers.client.effects;

import net.dndats.hackersandslashers.common.setup.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;

public class VisualEffects {

    /**
     * These methods are responsible to call particle effects easier.
     */

    public static void spawnCriticalParticle(Level level, double x, double y, double z, DamageSource damageSource) {
        if (damageSource.is(DamageTypes.GENERIC)
                || damageSource.is(DamageTypes.PLAYER_ATTACK)
                || damageSource.is(DamageTypes.ARROW)) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ModParticles.CRIT_GENERIC.get(),
                        x, y, z,
                        1,
                        0, 0, 0,
                        0
                );
            } else {
                level.addParticle(
                        ModParticles.CRIT_GENERIC.get(),
                        x, y, z,
                        0,
                        0,
                        0
                );
            }
        } else if (damageSource.getClass().getSimpleName().contains("Spell")) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ModParticles.CRIT_MAGIC.get(),
                        x, y, z,
                        1,
                        0, 0, 0,
                        0
                );
            } else {
                level.addParticle(
                        ModParticles.CRIT_MAGIC.get(),
                        x, y, z,
                        0,
                        0,
                        0
                );
            }
        }
    }

    public static void spawnAttackParticles(Level level, double x, double y, double z, int damageAmount) {
        if (damageAmount > 20) damageAmount = 20;
        if (damageAmount < 5) damageAmount = 5;
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ModParticles.ATTACK_SPARK.get(),
                    x, y, z,
                    damageAmount,
                    0, 0, 0,
                    1
            );
        }
        for (int i = 0; i < 5; i++) {
            level.addParticle(
                    ModParticles.ATTACK_SPARK.get(),
                    x, y, z,
                    (level.random.nextDouble() - 0.5) * 0.5,
                    (level.random.nextDouble() - 0.5) * 0.5,
                    (level.random.nextDouble() - 0.5) * 0.5
            );
        }
    }

    public static void spawnAttackCritParticles(Level level, double x, double y, double z, int damageAmount) {
        if (damageAmount > 20) damageAmount = 20;
        if (damageAmount < 5) damageAmount = 5;
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ModParticles.ATTACK_SPARK_CRIT_GENERIC.get(),
                    x, y, z,
                    damageAmount,
                    0, 0, 0,
                    1
            );
        }
        for (int i = 0; i < 5; i++) {
            level.addParticle(
                    ModParticles.ATTACK_SPARK_CRIT_GENERIC.get(),
                    x, y, z,
                    (level.random.nextDouble() - 0.5) * 0.5,
                    (level.random.nextDouble() - 0.5) * 0.5,
                    (level.random.nextDouble() - 0.5) * 0.5
            );
        }
    }

}
