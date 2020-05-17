package nstu.javaprog.model;

import nstu.javaprog.util.Properties;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ProbabilisticCreator {
    private static long idPool = 0;
    protected final AtomicInteger elementCounter = new AtomicInteger();
    protected Habitat habitat = null;
    protected Properties properties;

    public ProbabilisticCreator(Properties properties) {
        this.properties = properties;
    }

    protected static long getNextId() {
        return idPool++;
    }

    static void resetIdPool() {
        idPool = 0;
    }

    final void prepare(Habitat habitat) {
        this.habitat = habitat;
    }

    public abstract Fish createElement();

    final int getElementsNumber() {
        return elementCounter.get();
    }

    final void resetCounter() {
        elementCounter.set(0);
    }

    final Properties getProperties() {
        return properties;
    }

    final void setProperties(Properties properties) {
        this.properties = properties;
    }
}
