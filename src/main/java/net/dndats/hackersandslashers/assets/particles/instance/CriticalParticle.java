package net.dndats.hackersandslashers.assets.particles.instance;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CriticalParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    public CriticalParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(2f, 2f);
        this.quadSize *= 2;
        this.lifetime = 20;
        this.gravity = -0.1f;
        this.hasPhysics = false;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
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
        if (this.age < this.lifetime) {
            this.quadSize *= 1.0f - ((float) this.age / (float) this.lifetime);
        }
    }

}
