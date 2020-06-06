package ru.nstu.javaprog.model;

import ru.nstu.javaprog.api.FishType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public abstract class Fish implements Drawable, Movable, Serializable {
    private static final long serialVersionUID = 1L;
    private final long id;
    private final int lifetime;
    protected int x, y;
    protected int xSpeed, ySpeed;

    public Fish(long id, int x, int y, int xSpeed, int ySpeed, int lifetime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.lifetime = lifetime;
        validate();
    }

    private void validate() {
        if (id < 0)
            throw new IllegalArgumentException("id < 0");
        if (x < 0)
            throw new IllegalArgumentException("x < 0");
        if (y < 0)
            throw new IllegalArgumentException("y < 0");
        if (lifetime <= 0)
            throw new IllegalArgumentException("lifetime <= 0");
    }

    protected final void normalize(int xMax, int yMax, int picWidth, int picHeight) {
        int centredX = x + picWidth / 2;
        if (centredX < picWidth / 2 || centredX > xMax - picWidth / 2) {
            centredX = Math.abs(centredX - picWidth / 2) <
                    Math.abs(centredX - xMax + picWidth / 2)
                    ? picWidth / 2
                    : xMax - picWidth / 2;
            xSpeed = -xSpeed;
        }
        x = centredX - picWidth / 2;

        int centredY = y + picHeight / 2;
        if (centredY < picHeight / 2 || centredY > yMax - picHeight / 2) {
            centredY = Math.abs(centredY - picHeight / 2) <
                    Math.abs(centredY - yMax + picHeight / 2)
                    ? picHeight / 2
                    : yMax - picHeight / 2;
            ySpeed = -ySpeed;
        }
        y = centredY - picHeight / 2;
    }

    private void rotate() {

    }

    public abstract FishType getFishType();

    public final long getId() {
        return id;
    }

    public final int getLifetime() {
        return lifetime;
    }

    public final int getY() {
        return y;
    }

    public final int getX() {
        return x;
    }

    public final int getXSpeed() {
        return xSpeed;
    }

    public final int getYSpeed() {
        return ySpeed;
    }

    private void readObject(ObjectInputStream inputStream)
            throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        validate();
    }
}