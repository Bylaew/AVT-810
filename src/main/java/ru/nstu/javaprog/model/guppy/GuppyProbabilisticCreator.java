package ru.nstu.javaprog.model.guppy;

import ru.nstu.javaprog.model.ProbabilisticCreator;
import ru.nstu.javaprog.util.Coordinates;
import ru.nstu.javaprog.util.Properties;
import ru.nstu.javaprog.view.ViewContainer;

public final class GuppyProbabilisticCreator extends ProbabilisticCreator {
    public GuppyProbabilisticCreator(Properties properties) {
        super(properties);
    }

    @Override
    public Guppy createEntity() {
        Guppy newEntity = null;
        if (Float.compare((float) Math.random(), properties.getChance()) <= 0
                && habitat.getTime() % properties.getDelay() == 0) {
            Coordinates coordinates = ViewContainer.getRandomCoordinates();
            newEntity = new Guppy(
                    getNextId(),
                    coordinates.getX(),
                    coordinates.getY(),
                    (int) (Math.random() * (properties.getMaxSpeed() - properties.getMinSpeed()))
                            + properties.getMinSpeed(),
                    (int) (Math.random() * (properties.getMaxSpeed() - properties.getMinSpeed()))
                            + properties.getMinSpeed(),
                    properties.getLifetime()
            );
            entityCounter.incrementAndGet();
        }
        return newEntity;
    }
}