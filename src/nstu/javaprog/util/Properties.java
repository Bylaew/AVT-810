package nstu.javaprog.util;

import nstu.javaprog.exception.IllegalPropertiesException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public final class Properties implements Serializable {
    private static final long serialVersionUID = 1L;
    private final float chance;
    private final int delay;
    private final int maxSpeed;
    private final int minSpeed;
    private final int lifetime;
    private final int priority;

    private Properties(
            float chance,
            int delay,
            int minSpeed,
            int maxSpeed,
            int lifetime,
            int priority
    ) {
        this.delay = delay;
        this.chance = chance;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.lifetime = lifetime;
        this.priority = priority;
        validate();
    }

    public static Properties buildPropertiesWithCheckedException(
            float chance,
            int delay,
            int minSpeed,
            int maxSpeed,
            int lifetime,
            int priority
    ) throws IllegalPropertiesException {
        try {
            return new Properties(
                    chance,
                    delay,
                    minSpeed,
                    maxSpeed,
                    lifetime,
                    priority
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalPropertiesException(exception.getMessage());
        }
    }

    public static Properties buildPropertiesWithUncheckedException(
            float chance,
            int delay,
            int minSpeed,
            int maxSpeed,
            int lifetime,
            int priority
    ) {
        return new Properties(
                chance,
                delay,
                minSpeed,
                maxSpeed,
                lifetime,
                priority
        );
    }

    private void validate() {
        if (Float.compare(chance, 0.f) < 0 ||
                Float.compare(chance, 1.f) > 0)
            throw new IllegalArgumentException(
                    "Invalid chance format. Chance must be > 0 and < 1"
            );

        if (minSpeed <= 0)
            throw new IllegalArgumentException(
                    "Invalid minimal speed format. Minimal speed must be > 0"
            );

        if (maxSpeed <= 0)
            throw new IllegalArgumentException(
                    "Invalid maximal speed format. Maximal speed must be > 0"
            );

        if (delay <= 0)
            throw new IllegalArgumentException(
                    "Invalid delay format. Delay must be > 0"
            );

        if (lifetime <= 0)
            throw new IllegalArgumentException(
                    "Invalid lifetime format. Lifetime must be > 0"
            );

        if (minSpeed > maxSpeed)
            throw new IllegalArgumentException(
                    "Invalid speed format. Minimal speed must be <= maximal one"
            );

        if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY)
            throw new IllegalArgumentException(
                    "Invalid priority format. Priority must be >= "
                            + Thread.MIN_PRIORITY + " and <= " + Thread.MAX_PRIORITY
            );
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

    @Override
    public String toString() {
        return "Probability: " + chance + "\n" +
                "Delay: " + delay + "\n" +
                "Minimal speed: " + minSpeed + "\n" +
                "Maximal speed: " + maxSpeed + "\n" +
                "Lifetime: " + lifetime + "\n" +
                "Priority: " + priority;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        try {
            validate();
        } catch (IllegalArgumentException exception) {
            throw new IOException(new IllegalPropertiesException(exception.getMessage()));
        }
    }
}