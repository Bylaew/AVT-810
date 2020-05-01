package LabObjects;


import java.awt.image.BufferedImage;


public class Kap_House extends House {
    Kap_House(int height, int width, int ID){
        this.ID=ID;
        setX(x = (int) (Math.random()*(width-image.getInstance().kap_image.getWidth())));
        setY(y = (int) (Math.random()*(height-image.getInstance().kap_image.getHeight())));
        }

    @Override
    public BufferedImage getImage() {
        return image.getInstance().kap_image;
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
    public String getType(){return "Kap";}

}
