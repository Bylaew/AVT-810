package nstu.javaprog.lab1.view.element;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nstu.javaprog.lab1.util.ImageReader.readImage;

public class Guppy extends Fish {
    private static final BufferedImage FISH_IMAGE = readImage("./resources/guppy.png");

    public Guppy(int x, int y, int xSpeed, int ySpeed) {
        super(x, y, xSpeed, ySpeed);
    }

    @Override
    public void normalize(int xMax, int yMax) {
        int centredY = y + FISH_IMAGE.getHeight() / 2;
        if (centredY < FISH_IMAGE.getHeight() / 2 || centredY > yMax - FISH_IMAGE.getHeight() / 2)
            ySpeed = -ySpeed;
    }

    @Override
    public void move() {
        y += ySpeed;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FISH_IMAGE, x, y, null);
    }
}
