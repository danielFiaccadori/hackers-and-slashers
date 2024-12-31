package net.dndats.hackersandslashers.assets.particles;

import net.dndats.hackersandslashers.assets.particles.instance.SmallCriticalParticle;
import net.dndats.hackersandslashers.assets.particles.providers.CriticalParticleProvider;
import net.dndats.hackersandslashers.assets.particles.providers.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SmallCriticalParticleProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public SmallCriticalParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new SmallCriticalParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
    }

    @SubscribeEvent
    public static void register(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SMALL_CRITICAL_PARTICLE.get(), CriticalParticleProvider::new);
    }

}
