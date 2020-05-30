package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Warrior extends Ant implements Serializable {
    private static transient Image img = new ImageIcon("src/res/Worker.jpg").getImage();
    private int oldX;
    private int oldY;
    private boolean isStopX = false;
    private boolean isStopY = false;

    public static int count = 0;

    Warrior(int x, int y, int bornTime, int lifeTime ,int ingef) {
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

    public int[] toMas() {
        int a[] = new int[]{1,this.x, this.y, this.lifeTime, this.bornTime, this.indef, this.lifeTime};
        System.out.println(a.toString());
        return a;
    }


}


