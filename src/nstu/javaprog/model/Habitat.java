package nstu.javaprog.model;

import javafx.util.Pair;
import nstu.javaprog.model.gold.GoldBehavior;
import nstu.javaprog.model.gold.GoldProbabilisticCreator;
import nstu.javaprog.model.guppy.GuppyBehavior;
import nstu.javaprog.model.guppy.GuppyProbabilisticCreator;
import nstu.javaprog.util.Properties;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Habitat {
    private final TickGenerator generator = new TickGenerator();
    private ProbabilisticCreator goldCreator = null;
    private ProbabilisticCreator guppyCreator = null;
    private Behavior goldBehavior = null;
    private Behavior guppyBehavior = null;

    public final void prepare() {
        goldCreator = new GoldProbabilisticCreator(new Properties(
                1.0f,
                1,
                1,
                1,
                5,
                Thread.NORM_PRIORITY
        ));
        goldCreator.prepare(this);

        guppyCreator = new GuppyProbabilisticCreator(new Properties(
                1.0f,
                1,
                1,
                1,
                5,
                Thread.NORM_PRIORITY
        ));
        guppyCreator.prepare(this);

        goldBehavior = new GoldBehavior(33);
        goldBehavior.prepare(this);

        guppyBehavior = new GuppyBehavior(33);
        guppyBehavior.prepare(this);

        generator.schedule();
    }

    public void doForEachElement(Consumer<? super Fish> consumer) {
        ElementsKeeper.INSTANCE.doForEachElement(consumer);
    }

    public int getTime() {
        return generator.getTime();
    }

    public String getStatistic() {
        return "Current: " + ElementsKeeper.INSTANCE.getSize() + '\n' +
                "Total golds: " + goldCreator.getElementsNumber() + '\n' +
                "Total guppies: " + guppyCreator.getElementsNumber() + '\n' +
                "Time: " + getTime();
    }

    public Properties getGoldProperties() {
        return goldCreator.getProperties();
    }

    public void setGoldProperties(Properties properties) {
        goldCreator.setProperties(properties);
        goldBehavior.changePriority(properties.getPriority());
    }

    public Properties getGuppyProperties() {
        return guppyCreator.getProperties();
    }

    public void setGuppyProperties(Properties properties) {
        guppyCreator.setProperties(properties);
        guppyBehavior.changePriority(properties.getPriority());
    }

    public void activateGeneration() {
        generator.activate();
    }

    public void deactivateGeneration() {
        generator.deactivate();
    }

    public void activateGolds() {
        goldBehavior.activate();
    }

    public void deactivateGolds() {
        goldBehavior.deactivate();
    }

    public void activateGuppies() {
        guppyBehavior.activate();
    }

    public void deactivateGuppies() {
        guppyBehavior.deactivate();
    }

    public List<Pair<Long, Integer>> getAliveElements() {
        return ElementsKeeper.INSTANCE.getAliveElements();
    }

    public boolean isGenerationActivated() {
        return generator.isActivated();
    }

    public boolean isGoldsActivated() {
        return goldBehavior.isActivated();
    }

    public boolean isGuppiesActivated() {
        return guppyBehavior.isActivated();
    }

    public void reset() {
        generator.time.set(0);
        goldCreator.resetCounter();
        guppyCreator.resetCounter();
        ProbabilisticCreator.resetIdPool();
        ElementsKeeper.INSTANCE.removeAll();
    }

    private class TickGenerator {
        private final AtomicInteger time = new AtomicInteger();
        private volatile boolean isSuspended = true;

        void schedule() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended) {
                        Fish element;

                        ElementsKeeper.INSTANCE.removeDeadElements(getTime());

                        if ((element = goldCreator.createElement()) != null)
                            ElementsKeeper.INSTANCE.add(element, getTime());

                        if ((element = guppyCreator.createElement()) != null)
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

        boolean isActivated() {
            return !isSuspended;
        }
    }
}
