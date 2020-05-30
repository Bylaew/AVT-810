package com.company;

import javax.swing.*;
import java.awt.*;

public class Yur_L extends Person {
    private static int count;
    private Image img;

    Yur_L() {
        count++;
    }

    Yur_L(float x, float y) {
        super(x, y);
        count++;
        img = new ImageIcon("C:\\Tp\\src\\com\\company\\Yur.jpg").getImage();
    }

    @Override
    public Image getImage() {
        return img;
    }

}
