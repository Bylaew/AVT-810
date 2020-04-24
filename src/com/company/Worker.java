package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Worker extends Ant{
    private static int count;
    private Image img;
    Worker(){
        count++;
    };
    Worker(float x, float y,int bornTime,int lifeTime,int indef){
        super(x,y,bornTime,lifeTime,indef);
        count++;
        img =  new ImageIcon("src/res/Worker.jpg").getImage();

    }

    @Override
    public Image getImage() {
        return img;
    }
}
