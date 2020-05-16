package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Worker extends Ant implements Serializable {
    private static int count;
    private static transient Image img =new ImageIcon("src/res/Worker.jpg").getImage(); ;
    Worker(){
        count++;
    };
    Worker(float x, float y,int bornTime,int lifeTime,int indef){
        super(x,y,bornTime,lifeTime,indef);
        count++;
        img =  new ImageIcon("src/res/Worker.jpg").getImage();

    }


    public  Image getImage() {
        return img;
    }
}
