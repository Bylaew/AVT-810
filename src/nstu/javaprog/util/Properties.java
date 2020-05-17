package nstu.javaprog.util;

public final class Properties {
    private final float chance;
    private final int delay;
    private final int maxSpeed;
    private final int minSpeed;
    private final int lifetime;
    private final int priority;

    public Properties(
            float chance,
            int delay,
            int minSpeed,
            int maxSpeed,
            int lifetime,
            int priority
    ) {
        if (minSpeed <= 0)
            throw new IllegalArgumentException(
                    "Invalid minimal speed format\n" +
                            "Minimal speed must be > 0"
            );

        if (maxSpeed <= 0)
            throw new IllegalArgumentException(
                    "Invalid maximal speed format\n" +
                            "Maximal speed must be > 0"
            );

        if (delay <= 0)
            throw new IllegalArgumentException(
                    "Invalid delay format\n" +
                            "Delay must be > 0"
            );

        if (lifetime <= 0)
            throw new IllegalArgumentException(
                    "Invalid lifetime format\n" +
                            "Lifetime must be > 0"
            );

        if (minSpeed > maxSpeed)
            throw new IllegalArgumentException(
                    "Invalid speed format\n" +
                            "Minimal speed must be <= maximal one"
            );

        if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY)
            throw new IllegalArgumentException(
                    "Invalid priority format\n" +
                            "Priority must be >= " +
                            Thread.MIN_PRIORITY +
                            " and <= " +
                            Thread.MAX_PRIORITY
            );

        this.delay = delay;
        this.chance = chance;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.lifetime = lifetime;
        this.priority = priority;
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

    public int getLifetime() {
        return lifetime;
    }

    public int getPriority() {
        return priority;
    }
}