package ru.nstu.javaprog.model;

@FunctionalInterface
public interface Creator {
    Fish createFish(long id, int x, int y, int xSpeed, int ySpeed, int lifetime);
}
