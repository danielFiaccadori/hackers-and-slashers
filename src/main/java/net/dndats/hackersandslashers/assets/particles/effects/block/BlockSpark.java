package net.dndats.hackersandslashers.assets.particles.effects.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BlockSpark extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    public BlockSpark(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.1F, 0.1F);
        this.quadSize = 0.20F;
        this.lifetime = 20;
        this.gravity = 0.5F;
        this.hasPhysics = false;
        double spread = 0.25;
        this.xd = (this.random.nextDouble() - 0.5) * spread;
        this.yd = (this.random.nextDouble() - 0.5) * spread;
        this.zd = (this.random.nextDouble() - 0.5) * spread;
        this.rCol = 1;
        this.gCol = 1;
        this.bCol = 0;
        this.pickSprite(spriteSet);
    }

    @Override
    public int getLightColor(float partialTick) {
        int baseLight = 15728880;
        float fadeFactor = 1.0f - ((float) this.age / (float) this.lifetime);
        int fadedLight = (int) (baseLight * fadeFactor);
        return fadedLight;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age < this.lifetime) {
            this.quadSize *= 1f - ((float) this.age / (float) this.lifetime);
            float progress = (float) this.age / (float) this.lifetime;
            float initialRollSpeed = 10F;
            float finalRollSpeed = 5F;
            float currentRollSpeed = initialRollSpeed + (finalRollSpeed - initialRollSpeed) * progress;
            this.roll += (float) Math.toRadians(currentRollSpeed);
        }
    }

}
