package nstu.javaprog.model;

import nstu.javaprog.util.Coordinates;
import nstu.javaprog.view.ViewContainer;

final class GoldProbabilisticCreator extends ProbabilisticCreator {
    private final Habitat habitat;

    GoldProbabilisticCreator(
            float chance,
            int delay,
            int minSpeed,
            int maxSpeed,
            int lifetime,
            Habitat habitat
    ) {
        super(chance, delay, minSpeed, maxSpeed, lifetime);
        this.habitat = habitat;
    }

    @Override
    Gold createCanvasElement() {
        Gold newElement = null;
        if (Float.compare((float) Math.random(), properties.getChance()) <= 0
                && habitat.getTime() % properties.getDelay() == 0) {
            Coordinates coordinates = ViewContainer.getRandomCoordinates();
            newElement = new Gold(
                    ids++,
                    coordinates.getX(),
                    coordinates.getY(),
                    (int) (Math.random() * (properties.getMaxSpeed() - properties.getMinSpeed()))
                            + properties.getMinSpeed(),
                    (int) (Math.random() * (properties.getMaxSpeed() - properties.getMinSpeed()))
                            + properties.getMinSpeed(),
                    properties.getLifetime()
            );
            objectCounter.incrementAndGet();
        }
        return newElement;
    }
}
