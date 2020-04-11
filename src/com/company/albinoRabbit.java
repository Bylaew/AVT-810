package com.company;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class albinoRabbit extends AbstractRabbit {

    private int x,y;
    private BufferedImage image;

    albinoRabbit(int x, int y) {
        this.x=x;
        this.y=y;
    }

    @Override
    void go() {
        System.out.println("Кролик альбинос идет");
    }

    @Override
    void stay() {
        System.out.println("Кролик альбинос стоит");
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
