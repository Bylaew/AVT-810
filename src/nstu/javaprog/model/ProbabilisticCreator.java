package nstu.javaprog.model;

import nstu.javaprog.util.Properties;

import java.util.concurrent.atomic.AtomicInteger;

abstract class ProbabilisticCreator {
    final AtomicInteger objectCounter = new AtomicInteger();
    float chance;
    int delay;
    int maxSpeed;
    int minSpeed;

    ProbabilisticCreator(float chance, int delay, int minSpeed, int maxSpeed) {
        if (minSpeed > maxSpeed)
            throw new IllegalArgumentException("minSpeed > maxSpeed");
        this.chance = chance;
        this.delay = delay;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    abstract CanvasElement createCanvasElement();

    int getObjectCounter() {
        return objectCounter.get();
    }

    void resetCounter() {
        objectCounter.set(0);
    }

    Properties getProperties() {
        return new Properties(chance, delay, minSpeed, maxSpeed);
    }

    void setProperties(Properties properties) {
        this.delay = properties.getDelay();
        this.chance = properties.getChance();
        this.minSpeed = properties.getMinSpeed();
        this.maxSpeed = properties.getMaxSpeed();
    }
}
