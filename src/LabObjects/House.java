package LabObjects;


import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class House implements IBehaviour{
    protected int x,y;
    protected int ID;
    protected boolean goal=false;
    abstract public String getType();
    protected static DownloadImage image;
    abstract public BufferedImage getImage();
    public int getID(){return ID;}
    public void setGoal(boolean goal){this.goal=goal;}
}
