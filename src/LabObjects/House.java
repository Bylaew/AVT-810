package LabObjects;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

abstract public class House implements IBehaviour, Serializable {
    protected int x,y;
    protected int ID;
    protected boolean goal=false;
    abstract public String getType();
    protected transient static DownloadImage image;
    abstract public BufferedImage getImage();
    public int getID(){return ID;}
    public void setGoal(boolean goal){this.goal=goal;}
    public void setID(int ID){this.ID = ID;}
}
