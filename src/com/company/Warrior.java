package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Warrior extends Ant implements Serializable {
    private static int count;
    private static transient Image img = new ImageIcon("src/res/Worker.jpg").getImage();
    private float oldX;
    private float oldY;
    private boolean isStopX = false;
    private boolean isStopY = false;
    Warrior() {
        count++;

    }

    Warrior(float x, float y, int lifeTime, int bornTime,int ingef) {
        super(x, y,bornTime,lifeTime,ingef);
        oldX = x;
        oldY = y;
        count++;
            img = new ImageIcon("src/res/Warr.jpg").getImage();

    }

    public float getOldX() {
        return oldX;
    }

    public float getOldY() {
        return oldY;
    }

    public void setStopX(boolean stop) {
        isStopX = stop;
    }

    public boolean getStopX() {
        return isStopX;
    }
    public void setStopY(boolean stop) {
        isStopY = stop;
    }

    public boolean getStopY() {
        return isStopY;
    }

    public  Image getImage() {
        return img;
    }

}


