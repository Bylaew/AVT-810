package nstu.javaprog.model;

import nstu.javaprog.util.Properties;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Habitat {
    private final TickGenerator generator = new TickGenerator();
    private final ProbabilisticCreator goldCreator;
    private final ProbabilisticCreator guppyCreator;

    public Habitat() {
        goldCreator = new GoldProbabilisticCreator(1.0f, 1, 1, 1, this);
        guppyCreator = new GuppyProbabilisticCreator(1.0f, 1, 1, 1, this);
    }

    public void doForEachElement(Consumer<? super CanvasElement> consumer) {
        ElementsKeeper.INSTANCE.foreach(consumer);
    }

    public long getTime() {
        return generator.getTime();
    }

    public String getStatistic() {
        return "Total: " + ElementsKeeper.INSTANCE.getSize() + '\n' +
                "Golds: " + goldCreator.getObjectCounter() + '\n' +
                "Guppies: " + guppyCreator.getObjectCounter() + '\n' +
                "Time: " + getTime();
    }

    public Properties getGoldProperties() {
        return goldCreator.getProperties();
    }

    public Properties getGuppyProperties() {
        return guppyCreator.getProperties();
    }

    public void setGoldProperties(Properties properties) {
        goldCreator.setProperties(validateData(properties));
    }

    public void setGuppyProperties(Properties properties) {
        guppyCreator.setProperties(validateData(properties));
    }

    public void activate() {
        generator.activate();
    }

    public void deactivate() {
        generator.deactivate();
    }

    public boolean isLaunched() {
        return !generator.isSuspended;
    }

    public void reset() {
        generator.time.set(0);
        goldCreator.resetCounter();
        guppyCreator.resetCounter();
        ElementsKeeper.INSTANCE.removeAll();
    }

    private Properties validateData(Properties properties) {
        if (properties.getMinSpeed() > properties.getMaxSpeed())
            throw new IllegalArgumentException(
                    "Invalid speed format\n" +
                            "Minimal speed must be <= then maximal one"
            );
        return properties;
    }

    private class TickGenerator {
        private final AtomicInteger time = new AtomicInteger();
        private volatile boolean isSuspended = true;

        TickGenerator() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended) {
                        CanvasElement element;
                        if ((element = goldCreator.createCanvasElement()) != null)
                            ElementsKeeper.INSTANCE.add(element);
                        if ((element = guppyCreator.createCanvasElement()) != null)
                            ElementsKeeper.INSTANCE.add(element);
                        time.incrementAndGet();
                    }
                }
            }, 0, 1000);
        }

        long getTime() {
            return time.get();
        }

        void activate() {
            isSuspended = false;
        }

        void deactivate() {
            isSuspended = true;
        }
    }
}
