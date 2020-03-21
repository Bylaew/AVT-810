package com.company;

import javax.swing.*;
import java.awt.*;

public class Warrior extends Ant {
    private static int count;
    private Image img;

    Warrior() {
        count++;

    }

    Warrior(float x, float y) {
        super(x, y);
        count++;
            img = new ImageIcon("src/res/Warrior.jpeg").getImage();

    }

    @Override
    public Image getImage() {
        return img;
    }

}


