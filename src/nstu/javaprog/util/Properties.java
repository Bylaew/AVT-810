package nstu.javaprog.util;

public final class Properties {
    private final float chance;
    private final int delay;
    private final int maxSpeed;
    private final int minSpeed;

    public Properties(
            float chance,
            int delay,
            int minSpeed,
            int maxSpeed
    ) {
        this.delay = delay;
        this.chance = chance;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public float getChance() {
        return chance;
    }

    public int getDelay() {
        return delay;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getMinSpeed() {
        return minSpeed;
    }
}