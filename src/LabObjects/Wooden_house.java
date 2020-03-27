package LabObjects;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Wooden_house extends House{
    Wooden_house(int height, int width){
        try{
            File file_wooden= new File("./resources/Wooden.png");
            image = ImageIO.read(file_wooden);
        } catch (IOException e) {
            System.out.println("Файл не найден");}
        x = (int) (Math.random()*(width-image.getWidth()));
        y = (int) (Math.random()*(height-image.getHeight()));
        }

    @Override
    public void draw(Graphics g){
        g.drawImage(image, x,y, null );
    }
}
