package nstu.javaprog.model;

import nstu.javaprog.util.Properties;

import java.util.concurrent.atomic.AtomicInteger;

abstract class ProbabilisticCreator {
    static long ids = 0;
    final AtomicInteger objectCounter = new AtomicInteger();
    Properties properties;

    ProbabilisticCreator(float chance, int delay, int minSpeed, int maxSpeed, int lifetime) {
        if (minSpeed > maxSpeed)
            throw new IllegalArgumentException("minSpeed > maxSpeed");
        properties = new Properties(chance, delay, minSpeed, maxSpeed, lifetime);
    }

    abstract Fish createCanvasElement();

    int getObjectCounter() {
        return objectCounter.get();
    }

    void resetCounter() {
        objectCounter.set(0);
    }

    Properties getProperties() {
        return properties;
    }

    void setProperties(Properties properties) {
        this.properties = properties;
    }
}
