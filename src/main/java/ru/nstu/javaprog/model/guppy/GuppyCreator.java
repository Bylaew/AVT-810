package ru.nstu.javaprog.model.guppy;

import ru.nstu.javaprog.model.Creator;

public final class GuppyCreator implements Creator {
    @Override
    public Guppy createFish(long id, int x, int y, int xSpeed, int ySpeed, int lifetime) {
        return new Guppy(id, x, y, xSpeed, ySpeed, lifetime);
    }
}
