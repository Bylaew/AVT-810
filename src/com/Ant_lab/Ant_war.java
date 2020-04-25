package com.Ant_lab;

import javax.swing.*;
import java.awt.*;

public class Ant_war extends Ant
{
    private static int count;
    private Image image;

    public Ant_war()
    {
        count++;
    }

    public Ant_war(float x,float y)
    {
        super(x,y);
        count++;
        image = new ImageIcon("src/com/Ant_lab/ant_warior.png").getImage();
    }

    @Override
    public Image getImage()
    {
        return image;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

}