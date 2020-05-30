package com.company;

import javax.swing.*;
import java.awt.*;

public class Fiz_L extends Person {
    private static int count;
    private Image img;

    Fiz_L() {
        count++;
    }

    Fiz_L(float x, float y) {
        super(x, y);
        count++;
        img = new ImageIcon("C:\\Tp\\src\\com\\company\\Fiz.jpg").getImage();
    }

    @Override
    public Image getImage() {
        return img;
    }

}
