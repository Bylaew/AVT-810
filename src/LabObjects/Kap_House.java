package LabObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Kap_House extends House {
    Kap_House(int height, int width){

        try{
            File file_wooden= new File("./resources/Kap.png");
            image = ImageIO.read(file_wooden);
        } catch (IOException e) {
            System.out.println("Файл не найден");}
        setX(x = (int) (Math.random()*(width-image.getWidth())));
        setY(y = (int) (Math.random()*(height-image.getHeight())));
        }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null );
    }
    @Override
    public void setX(int x){this.x=x;}
    @Override
    public void setY(int y){this.y=y;}
    @Override
    public int getX(){return x;}
    @Override
    public int getY(){return y;}

}
