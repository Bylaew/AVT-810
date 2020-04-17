package nstu.javaprog.model;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nstu.javaprog.util.ImageReader.readImage;

final class Gold extends Fish {
    private static final BufferedImage FISH_IMAGE = readImage("./resources/gold.png");

    Gold(long id, int x, int y, int xSpeed, int ySpeed, int lifetime) {
        super(id, x, y, xSpeed, ySpeed, lifetime);
    }

    @Override
    public void normalize(int xMax, int yMax) {
        super.normalize(xMax, yMax, FISH_IMAGE.getWidth(), FISH_IMAGE.getHeight());
    }

    @Override
    public void move() {
        x += xSpeed;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FISH_IMAGE, x, y, null);
    }
}
