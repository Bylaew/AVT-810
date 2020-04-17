package nstu.javaprog.view;

import java.awt.*;

public interface CanvasElement {
    void move();

    void draw(Graphics g);

    void normalize(int xMax, int yMax);
}
