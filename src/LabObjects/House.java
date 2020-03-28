package LabObjects;


import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class House implements IBehaviour{
    protected int x,y;
    protected BufferedImage image;
    abstract public void draw(Graphics gr);
}
