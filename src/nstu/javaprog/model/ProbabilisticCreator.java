package nstu.javaprog.model;

import nstu.javaprog.util.Properties;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class ProbabilisticCreator {
    private static final AtomicLong idPool = new AtomicLong();
    protected final AtomicInteger entityCounter = new AtomicInteger();
    protected Habitat habitat = null;
    protected volatile Properties properties;

    public ProbabilisticCreator(Properties properties) {
        this.properties = properties;
    }

    protected static long getNextId() {
        return idPool.getAndIncrement();
    }

    static long getCurrentId() {
        return idPool.get();
    }

    static void resetIdPool() {
        idPool.set(0);
    }

    static void setPrimaryId(long primaryId) {
        idPool.set(primaryId);
    }

    final void prepare(Habitat habitat) {
        this.habitat = habitat;
    }

    public abstract Fish createEntity();

    final int getEntitiesNumber() {
        return entityCounter.get();
    }

    final void setEntitiesNumber(int value) {
        entityCounter.set(value);
    }

    final void resetEntitiesNumber() {
        entityCounter.set(0);
    }

    final Properties getProperties() {
        return properties;
    }

    final void setProperties(Properties properties) {
        this.properties = properties;
    }
}
