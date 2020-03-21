package nstu.javaprog.lab1.view.element;

import java.awt.*;

public interface CanvasElement {
    void move();

    void draw(Graphics g);

    void normalize(int xMax, int yMax);
}
