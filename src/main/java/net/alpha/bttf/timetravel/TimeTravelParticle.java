package net.alpha.bttf.timetravel;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TimeTravelParticle extends ParticleSmokeLarge
{

    private float flameScale;
    private static final ResourceLocation flame = new ResourceLocation("ftfloocraft", "textures/particle/torchflame.png");

    protected TimeTravelParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1201_8_, double p_i1201_10_, double p_i1201_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1201_8_, p_i1201_10_, p_i1201_12_);
    }



    public int getBrightnessForRender(float p_70070_1_)
    {
        float f1 = ((float)this.particleAge + p_70070_1_) / (float)this.particleMaxAge;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(p_70070_1_);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f1 * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_)
    {
        float f1 = ((float)this.particleAge + p_70013_1_) / (float)this.particleMaxAge;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        float f2 = this.getBrightness(p_70013_1_);
        return f2 * f1 + (1.0F - f1);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {

        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

}
