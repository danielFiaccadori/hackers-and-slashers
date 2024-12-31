package net.dndats.hackersandslashers.assets.particles.instance;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SmallCriticalParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    public SmallCriticalParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.1f, 0.1f);
        this.lifetime = 30;
        this.gravity = -0.1f;
        this.hasPhysics = true;
        double spread = 0.5;
        this.xd = (this.random.nextDouble() - 0.5) * spread;
        this.yd = (this.random.nextDouble() - 0.5) * spread;
        this.zd = (this.random.nextDouble() - 0.5) * spread;
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age < this.lifetime / 2) {
            this.xd *= 1.05;
            this.yd *= 1.05;
            this.zd *= 1.05;
        }
        if (this.age < this.lifetime) {
            this.quadSize *= 1f - ((float) this.age / (float) this.lifetime);
        }
    }

}
