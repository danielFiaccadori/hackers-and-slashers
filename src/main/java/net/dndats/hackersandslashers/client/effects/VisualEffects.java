package net.dndats.hackersandslashers.client.effects;

import net.dndats.hackersandslashers.assets.particles.providers.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class VisualEffects {

    public static void spawnCriticalParticle(Level level, double x, double y, double z, int damageAmount) {
        if (level instanceof ServerLevel serverLevel) {
            // Server-side particle spawning
            serverLevel.sendParticles(
                    ModParticles.CRITICAL_PARTICLE.get(),
                    x, y, z,
                    1,
                    0, 0, 0,
                    0
            );
            serverLevel.sendParticles(
                    ModParticles.SMALL_CRITICAL_PARTICLE.get(),
                    x, y, z,
                    10 + damageAmount,
                    0, 0, 0,
                    1
            );
        } else {
            level.addParticle(
                    ModParticles.CRITICAL_PARTICLE.get(),
                    x, y, z,
                    0,
                    0,
                    0
            );

            for (int i = 0; i < 20; i++) {
                level.addParticle(
                        ModParticles.SMALL_CRITICAL_PARTICLE.get(),
                        x, y, z,
                        (level.random.nextDouble() - 0.5) * 0.5,
                        (level.random.nextDouble() - 0.5) * 0.5,
                        (level.random.nextDouble() - 0.5) * 0.5
                );
            }
        }
    }

}
