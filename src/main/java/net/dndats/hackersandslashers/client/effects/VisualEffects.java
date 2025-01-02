package net.dndats.hackersandslashers.client.effects;

import net.dndats.hackersandslashers.assets.particles.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;

public class VisualEffects {

    public static void spawnCriticalParticle(Level level, double x, double y, double z, int damageAmount, DamageSource damageSource) {
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
                serverLevel.sendParticles(
                        ModParticles.CRIT_GENERIC_SMALL.get(),
                        x, y, z,
                        5 + damageAmount,
                        0, 0, 0,
                        0.5
                );
            } else {
                level.addParticle(
                        ModParticles.CRIT_GENERIC.get(),
                        x, y, z,
                        0,
                        0,
                        0
                );

                for (int i = 0; i < 5 + damageAmount; i++) {
                    level.addParticle(
                            ModParticles.CRIT_GENERIC_SMALL.get(),
                            x, y, z,
                            (level.random.nextDouble() - 0.5) * 0.5,
                            (level.random.nextDouble() - 0.5) * 0.5,
                            (level.random.nextDouble() - 0.5) * 0.5
                    );
                }
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
                serverLevel.sendParticles(
                        ModParticles.CRIT_MAGIC_SMALL.get(),
                        x, y, z,
                        5 + damageAmount,
                        0, 0, 0,
                        0.5
                );
            } else {
                level.addParticle(
                        ModParticles.CRIT_MAGIC.get(),
                        x, y, z,
                        0,
                        0,
                        0
                );

                for (int i = 0; i < 5 + damageAmount; i++) {
                    level.addParticle(
                            ModParticles.CRIT_MAGIC_SMALL.get(),
                            x, y, z,
                            (level.random.nextDouble() - 0.5) * 0.5,
                            (level.random.nextDouble() - 0.5) * 0.5,
                            (level.random.nextDouble() - 0.5) * 0.5
                    );
                }
            }
        }
    }

}
