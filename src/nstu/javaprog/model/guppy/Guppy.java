package nstu.javaprog.model.guppy;

import nstu.javaprog.model.Fish;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nstu.javaprog.util.ImageReader.readImage;

final class Guppy extends Fish {
    private static final BufferedImage FISH_IMAGE = readImage("./resources/guppy.png");

    Guppy(long id, int x, int y, int xSpeed, int ySpeed, int lifetime) {
        super(id, x, y, xSpeed, ySpeed, lifetime);
    }

    @Override
    public void normalize(int xMax, int yMax) {
        super.normalize(xMax, yMax, FISH_IMAGE.getWidth(), FISH_IMAGE.getHeight());
    }

    @Override
    public void move() {
        y += ySpeed;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(FISH_IMAGE, x, y, null);
    }
}
