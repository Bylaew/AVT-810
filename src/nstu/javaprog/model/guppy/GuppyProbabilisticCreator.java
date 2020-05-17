package nstu.javaprog.model.guppy;

import nstu.javaprog.model.ProbabilisticCreator;
import nstu.javaprog.util.Coordinates;
import nstu.javaprog.util.Properties;
import nstu.javaprog.view.ViewContainer;

public final class GuppyProbabilisticCreator extends ProbabilisticCreator {
    public GuppyProbabilisticCreator(Properties properties) {
        super(properties);
    }

    @Override
    public Guppy createElement() {
        Guppy newElement = null;
        if (Float.compare((float) Math.random(), properties.getChance()) <= 0
                && habitat.getTime() % properties.getDelay() == 0) {
            Coordinates coordinates = ViewContainer.getRandomCoordinates();
            newElement = new Guppy(
                    getNextId(),
                    coordinates.getX(),
                    coordinates.getY(),
                    (int) (Math.random() * (properties.getMaxSpeed() - properties.getMinSpeed()))
                            + properties.getMinSpeed(),
                    (int) (Math.random() * (properties.getMaxSpeed() - properties.getMinSpeed()))
                            + properties.getMinSpeed(),
                    properties.getLifetime()
            );
            elementCounter.incrementAndGet();
        }
        return newElement;
    }
}