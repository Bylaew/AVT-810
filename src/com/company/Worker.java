package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Worker extends Ant implements Serializable {
    private static transient Image img = new ImageIcon("src/res/Worker.jpg").getImage();
    public static int count = 0;

    Worker(int x, int y, int bornTime, int lifeTime, int indef) {
        super(x, y, bornTime, lifeTime, indef);
        img = new ImageIcon("src/res/Worker.jpg").getImage();
        count++;

    }

    public int[] toMas() {
        int a[] = new int[]{2, this.x, this.y, this.lifeTime, this.bornTime, this.indef, this.lifeTime};
        System.out.println(a.toString());
        return a;
    }


    public Image getImage() {
        return img;
    }
}
