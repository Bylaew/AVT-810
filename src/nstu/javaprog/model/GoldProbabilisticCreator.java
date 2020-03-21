package nstu.javaprog.model;

import nstu.javaprog.util.Coordinates;
import nstu.javaprog.view.ViewContainer;

final class GoldProbabilisticCreator extends ProbabilisticCreator {
    private final Habitat habitat;

    GoldProbabilisticCreator(float chance, int delay, int minSpeed, int maxSpeed, Habitat habitat) {
        super(chance, delay, minSpeed, maxSpeed);
        this.habitat = habitat;
    }

    @Override
    public Gold createCanvasElement() {
        Gold newElement = null;
        if (Float.compare((float) Math.random(), chance) <= 0 && habitat.getTime() % delay == 0) {
            Coordinates coordinates = ViewContainer.getRandomCoordinates();
            newElement = new Gold(
                    coordinates.getX(),
                    coordinates.getY(),
                    (int) (Math.random() * (maxSpeed - minSpeed)) + minSpeed,
                    (int) (Math.random() * (maxSpeed - minSpeed)) + minSpeed
            );
            objectCounter.incrementAndGet();
        }
        return newElement;
    }
}
