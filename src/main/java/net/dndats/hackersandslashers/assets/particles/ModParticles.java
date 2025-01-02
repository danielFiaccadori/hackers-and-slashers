package net.dndats.hackersandslashers.assets.particles;

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

    // GENERIC CRITICAL PARTICLE

    public static final Supplier<SimpleParticleType> CRIT_GENERIC = PARTICLES.register(
            "crit_generic",
            () -> new SimpleParticleType(false)
    );

    public static final Supplier<SimpleParticleType> CRIT_GENERIC_SMALL = PARTICLES.register(
            "crit_generic_small",
            () -> new SimpleParticleType(false)
    );

    // MAGIC CRITICAL PARTICLE

    public static final Supplier<SimpleParticleType> CRIT_MAGIC = PARTICLES.register(
            "crit_magic",
            () -> new SimpleParticleType(false)
    );

    public static final Supplier<SimpleParticleType> CRIT_MAGIC_SMALL = PARTICLES.register(
            "crit_magic_small",
            () -> new SimpleParticleType(false)
    );

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }

}
