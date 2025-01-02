package net.dndats.hackersandslashers.assets.particles.critical.type.generic.providers;

import net.dndats.hackersandslashers.assets.particles.ModParticles;
import net.dndats.hackersandslashers.assets.particles.critical.type.generic.instance.CritGenericSmall;
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
public class CritGenericSmallProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public CritGenericSmallProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new CritGenericSmall(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
    }

    @SubscribeEvent
    public static void register(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.CRIT_GENERIC_SMALL.get(), CritGenericSmallProvider::new);
    }

}
