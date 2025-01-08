package net.dndats.hackersandslashers.assets.particles.effects.attack.providers;

import net.dndats.hackersandslashers.common.setup.ModParticles;
import net.dndats.hackersandslashers.assets.particles.effects.attack.instance.AttackSparkCritGeneric;
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
public class AttackSparkCritGenericProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public AttackSparkCritGenericProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new AttackSparkCritGeneric(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
    }

    @SubscribeEvent
    public static void register(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ATTACK_SPARK_CRIT_GENERIC.get(), AttackSparkCritGenericProvider::new);
    }

}
