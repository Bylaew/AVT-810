package com.company;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class albinoRabbit extends AbstractRabbit {

    private transient BufferedImage image;
    //private BufferedImage

    albinoRabbit(int x, int y,long Birth,int ID) {
        this.x=x;
        this.y=y;
        this.BirthTime=Birth;
        this.ID=ID;

    }

    @Override
    void setCoordinates(int x,int y) {
        this.x=x;
        this.y=y;
    }

    @Override
    long getTimeBirth() {
        return BirthTime;
    }

    @Override
    int getID() {
        return ID;
    }

    @Override
    public BufferedImage getImage() {
        try {
            image= ImageIO.read(new File("rabbiAlbonod.png") );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
