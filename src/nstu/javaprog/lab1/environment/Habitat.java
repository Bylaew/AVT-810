package nstu.javaprog.lab1.environment;

import nstu.javaprog.lab1.view.canvas.MainWindow;
import nstu.javaprog.lab1.view.element.CanvasElement;
import nstu.javaprog.lab1.view.element.Gold;
import nstu.javaprog.lab1.view.element.Guppy;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Habitat {
    private final List<CanvasElement> objects = Collections.synchronizedList(new LinkedList<>());
    private TickGenerator generator = new TickGenerator();
    ProbabilisticCreator goldCreator = new GoldProbabilisticCreator();
    ProbabilisticCreator guppyCreator = new GuppyProbabilisticCreator();

    public void removeAll() {
        objects.clear();
    }

    public List<CanvasElement> getAll() {
        return objects;
    }

    public long getTime() {
        return generator.getTime();
    }

    public String getStatistic() {
        return "Total: " + objects.size() + '\n' +
                "Golds: " + goldCreator.getObjectCounter() + '\n' +
                "Guppies: " + guppyCreator.getObjectCounter() + '\n' +
                "Time: " + getTime();
    }

    public void activate() {
        generator.activate();
    }

    public void deactivate() {
        generator.deactivate();
    }

    private class TickGenerator {
        private AtomicInteger time = new AtomicInteger();
        private volatile boolean isSuspended = true;

        TickGenerator() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended) {
                        CanvasElement element;
                        if ((element = goldCreator.createCanvasElement()) != null)
                            objects.add(element);
                        if ((element = guppyCreator.createCanvasElement()) != null)
                            objects.add(element);
                        time.incrementAndGet();
                    }
                }
            }, 0, 1000);
        }

        long getTime() {
            return time.get();
        }

        void activate() {
            time.set(0);
            goldCreator.resetCounter();
            guppyCreator.resetCounter();
            isSuspended = false;
        }

        void deactivate() {
            isSuspended = true;
        }
    }

    private class GuppyProbabilisticCreator implements ProbabilisticCreator {
        private static final float CHANCE = 0.7f;
        private static final int DELAY = 3;
        private static final int MAX_SPEED = 4;
        private static final int MIN_SPEED = 1;
        private AtomicInteger objectCounter = new AtomicInteger();
        private Random random = new Random();

        @Override
        public Guppy createCanvasElement() {
            Guppy newElement = null;
            if (Float.compare(random.nextFloat(), CHANCE) <= 0 && getTime() % DELAY == 0) {
                newElement = new Guppy(MainWindow.WINDOW_WIDTH / 2 + random.nextInt() % (MainWindow.WINDOW_WIDTH / 4),
                        MainWindow.WINDOW_HEIGHT / 2 + random.nextInt() % (MainWindow.WINDOW_HEIGHT / 4),
                        Math.max(random.nextInt() % MAX_SPEED, MIN_SPEED),
                        Math.max(random.nextInt() % MAX_SPEED, MIN_SPEED));
                objectCounter.incrementAndGet();
            }
            return newElement;
        }

        public int getObjectCounter() {
            return objectCounter.get();
        }

        public void resetCounter() {
            objectCounter.set(0);
        }
    }

    private class GoldProbabilisticCreator implements ProbabilisticCreator {
        private static final float CHANCE = 0.4f;
        private static final int DELAY = 6;
        private static final int MAX_SPEED = 2;
        private static final int MIN_SPEED = 1;
        private AtomicInteger objectCounter = new AtomicInteger();
        private Random random = new Random();

        @Override
        public Gold createCanvasElement() {
            Gold newElement = null;
            if (Float.compare(random.nextFloat(), CHANCE) <= 0 && getTime() % DELAY == 0) {
                newElement = new Gold(MainWindow.WINDOW_WIDTH / 2 + random.nextInt() % (MainWindow.WINDOW_WIDTH / 4),
                        MainWindow.WINDOW_HEIGHT / 2 + random.nextInt() % (MainWindow.WINDOW_HEIGHT / 4),
                        Math.max(random.nextInt() % MAX_SPEED, MIN_SPEED),
                        Math.max(random.nextInt() % MAX_SPEED, MIN_SPEED));
                objectCounter.incrementAndGet();
            }
            return newElement;
        }

        public int getObjectCounter() {
            return objectCounter.get();
        }

        public void resetCounter() {
            objectCounter.set(0);
        }
    }
}
