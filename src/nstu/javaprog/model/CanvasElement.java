package nstu.javaprog.model;

import java.awt.*;

public interface CanvasElement {
    void move();

    void draw(Graphics g);

    void normalize(int xMax, int yMax);
}
