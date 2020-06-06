package ru.nstu.javaprog.model.gold;

import ru.nstu.javaprog.model.Creator;
import ru.nstu.javaprog.model.Fish;

public class GoldCreator implements Creator {
    @Override
    public Fish createFish(long id, int x, int y, int xSpeed, int ySpeed, int lifetime) {
        return new Gold(id, x, y, xSpeed, ySpeed, lifetime);
    }
}
