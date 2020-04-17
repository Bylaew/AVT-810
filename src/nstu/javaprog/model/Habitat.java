package nstu.javaprog.model;

import nstu.javaprog.util.Properties;
import nstu.javaprog.view.CanvasElement;

import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Habitat {
    private final TickGenerator generator = new TickGenerator();
    private final ProbabilisticCreator goldCreator;
    private final ProbabilisticCreator guppyCreator;

    public Habitat() {
        goldCreator = new GoldProbabilisticCreator(
                1.0f,
                1,
                1,
                1,
                5,
                this
        );
        guppyCreator = new GuppyProbabilisticCreator(
                1.0f,
                1,
                1,
                1,
                5,
                this
        );
    }

    public void doForEachElement(Consumer<CanvasElement> consumer) {
        ElementsKeeper.INSTANCE.doForEachElement(consumer);
    }

    public int getTime() {
        return generator.getTime();
    }

    public String getStatistic() {
        return "Current: " + ElementsKeeper.INSTANCE.getSize() + '\n' +
                "Total golds: " + goldCreator.getObjectCounter() + '\n' +
                "Total guppies: " + guppyCreator.getObjectCounter() + '\n' +
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

    public Object[] getAliveElementsInfo() {
        return ElementsKeeper.INSTANCE.getAliveElementsInfo();
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
        if (properties.getMinSpeed() <= 0)
            throw new IllegalArgumentException(
                    "Invalid minimal speed format\n" +
                            "Minimal speed must be > 0"
            );
        if (properties.getMaxSpeed() <= 0)
            throw new IllegalArgumentException(
                    "Invalid maximal speed format\n" +
                            "Maximal speed must be > 0"
            );
        if (properties.getDelay() <= 0)
            throw new IllegalArgumentException(
                    "Invalid delay format\n" +
                            "Delay must be > 0"
            );
        if (properties.getLifetime() <= 0)
            throw new IllegalArgumentException(
                    "Invalid lifetime format\n" +
                            "Lifetime must be > 0"
            );
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
                        Fish element;
                        ElementsKeeper.INSTANCE.removeDeadElements(getTime());
                        if ((element = goldCreator.createCanvasElement()) != null)
                            ElementsKeeper.INSTANCE.add(element, getTime());
                        if ((element = guppyCreator.createCanvasElement()) != null)
                            ElementsKeeper.INSTANCE.add(element, getTime());
                        time.incrementAndGet();
                    }
                }
            }, 0, 1000);
        }

        int getTime() {
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
