package com.company;

import javax.swing.*;
import java.awt.*;

public class Warrior extends Ant {
    private static int count;
    private Image img;

    Warrior() {
        count++;

    }

    Warrior(float x, float y, int lifeTime, int bornTime,int ingef) {
        super(x, y,bornTime,lifeTime,ingef);
        count++;
            img = new ImageIcon("src/res/Warr.jpg").getImage();

    }

    @Override
    public Image getImage() {
        return img;
    }

}


