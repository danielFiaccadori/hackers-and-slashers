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

    // CRITICAL HIT PARTICLES

    public static final Supplier<SimpleParticleType> CRIT_GENERIC = PARTICLES.register(
            "crit_generic",
            () -> new SimpleParticleType(false)
    );

    public static final Supplier<SimpleParticleType> ATTACK_SPARK_CRIT_GENERIC = PARTICLES.register(
            "attack_spark_crit_generic",
            () -> new SimpleParticleType(false)
    );

    public static final Supplier<SimpleParticleType> CRIT_MAGIC = PARTICLES.register(
            "crit_magic",
            () -> new SimpleParticleType(false)
    );

    public static final Supplier<SimpleParticleType> ATTACK_SPARK_CRIT_MAGIC = PARTICLES.register(
            "attack_spark_crit_magic",
            () -> new SimpleParticleType(false)
    );

    // EFFECT PARTICLES

    public static final Supplier<SimpleParticleType> ATTACK_SPARK = PARTICLES.register(
            "attack_spark",
            () -> new SimpleParticleType(false)
    );

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }

}
