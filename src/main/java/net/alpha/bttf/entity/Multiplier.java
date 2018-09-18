package net.alpha.bttf.entity;

public enum Multiplier
{

    NORMAL(1.1F, 6F);

    float accelerationMultiplier;
    float additionalMaxSpeed;


    Multiplier(float accelerationMultiplier, float additionalMaxSpeed)
    {
        this.accelerationMultiplier = accelerationMultiplier;
        this.additionalMaxSpeed = additionalMaxSpeed;
    }

    public float getAccelerationMultiplier()
    {
        return accelerationMultiplier;
    }

    public float getAdditionalMaxSpeed() {
        return additionalMaxSpeed;
    }

    public static Multiplier getMultiplier(int index)
    {
        if(index < 0 || index >= values().length)
        {
            return NORMAL;
        }
        return Multiplier.values()[index];
    }

}
