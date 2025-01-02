package net.dndats.hackersandslashers.assets.particles.enchantment.hearts_edge;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HeartsEdgeExecute extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    public HeartsEdgeExecute(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(2f, 2f);
        this.quadSize *= 15;
        this.lifetime = 5;
        this.gravity = 0;
        this.hasPhysics = false;
        this.rCol = 1;
        this.gCol = 0;
        this.bCol = 0;
        this.pickSprite(spriteSet);
    }

    @Override
    public int getLightColor(float partialTick) {
        return  15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteSet);
    }

}
