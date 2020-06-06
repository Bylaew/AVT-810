package ru.nstu.javaprog.model.gold;

import ru.nstu.javaprog.model.ProbabilisticCreator;
import ru.nstu.javaprog.util.Coordinates;
import ru.nstu.javaprog.util.Properties;
import ru.nstu.javaprog.view.ViewContainer;

public final class GoldProbabilisticCreator extends ProbabilisticCreator {
    public GoldProbabilisticCreator(Properties properties) {
        super(properties);
    }

    @Override
    public Gold createEntity() {
        Gold newEntity = null;
        if (Float.compare((float) Math.random(), properties.getChance()) <= 0
                && habitat.getTime() % properties.getDelay() == 0) {
            Coordinates coordinates = ViewContainer.getRandomCoordinates();
            newEntity = new Gold(
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
