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
    Worker(float x, float y){
        super(x,y);
        count++;
        img =  new ImageIcon("src/res/Worker.jpg").getImage();

    }

    @Override
    public Image getImage() {
        return img;
    }


}
