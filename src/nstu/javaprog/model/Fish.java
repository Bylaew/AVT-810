package nstu.javaprog.model;

import nstu.javaprog.view.CanvasElement;

abstract class Fish implements CanvasElement {
    private final long id;
    private final int lifetime;
    int x, y;
    int xSpeed, ySpeed;

    Fish(long id, int x, int y, int xSpeed, int ySpeed, int lifetime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.lifetime = lifetime;
    }

    final void normalize(int xMax, int yMax, int picWidth, int picHeight) {
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

    final long getId() {
        return id;
    }

    final int getLifetime() {
        return lifetime;
    }
}