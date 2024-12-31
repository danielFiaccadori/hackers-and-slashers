package net.dndats.hackersandslashers.client.effects;

import net.dndats.hackersandslashers.assets.particles.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class VisualEffects {

    public static void spawnCriticalParticle(Level level, double x, double y, double z) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ModParticles.CRITICAL_PARTICLE.get(),
                    x, y, z,
                    1,
                    0, 0, 0,
                    0
            );
        } else {
            level.addParticle(
                    ModParticles.CRITICAL_PARTICLE.get(),
                    x, y, z,
                    0, 0, 0
            );
        }
    }

}
