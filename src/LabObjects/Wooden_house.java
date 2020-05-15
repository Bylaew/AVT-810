package LabObjects;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Wooden_house extends House implements Serializable {
    private static final long serialVersionUID=2L;

    Wooden_house(int height, int width, int ID){
        this.ID=ID;
        setX(x = (int) (Math.random()*(width-image.getInstance().wood_image.getWidth())));
        setY(y = (int) (Math.random()*(height-image.getInstance().wood_image.getHeight())));
        }

    @Override
    public BufferedImage getImage(){
        return image.getInstance().wood_image;
    }
    @Override
    public void setX(int x){this.x=x;}
    @Override
    public void setY(int y){this.y=y;}
    @Override
    public int getX(){return x;}
    @Override
    public int getY(){return y;}
    @Override
    public String getType(){return "Wood";}


}
