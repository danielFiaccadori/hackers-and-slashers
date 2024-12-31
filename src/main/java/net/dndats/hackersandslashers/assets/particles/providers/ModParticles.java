package net.dndats.hackersandslashers.assets.particles.providers;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, HackersAndSlashers.MODID);

    public static final Supplier<SimpleParticleType> CRITICAL_PARTICLE = PARTICLES.register(
            "critical_particle",
            () -> new SimpleParticleType(false)
    );

    public static final Supplier<SimpleParticleType> SMALL_CRITICAL_PARTICLE = PARTICLES.register(
            "small_critical_particle",
            () -> new SimpleParticleType(false)
    );

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }

}
