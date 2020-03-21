package nstu.javaprog.lab1.view.element;

import java.awt.*;
import java.awt.image.BufferedImage;

import static nstu.javaprog.lab1.util.ImageReader.readImage;

public class Gold extends Fish {
    private static final BufferedImage FISH_IMAGE = readImage("./resources/gold.png");

    public Gold(int x, int y, int xSpeed, int ySpeed) {
        super(x, y, xSpeed, ySpeed);
    }

    @Override
    public void normalize(int xMax, int yMax) {
        int centredX = x + FISH_IMAGE.getWidth() / 2;
        if (centredX < FISH_IMAGE.getWidth() / 2 || centredX > xMax - FISH_IMAGE.getWidth() / 2)
            xSpeed = -xSpeed;
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
