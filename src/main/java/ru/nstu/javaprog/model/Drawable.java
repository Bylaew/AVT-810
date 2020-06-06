package ru.nstu.javaprog.model;

import java.awt.*;

interface Drawable {
    void draw(Graphics graphics);

    void normalize(int xMax, int yMax);
}
