import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Moto extends Vehicle {
    public static int count;
    private static BufferedImage image;
    static {try {
        image = ImageIO.read(new File("Moto.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }}

    Moto(float x, float y, int id){
        super(x,y, id);
        count++;
    }


    public Image getImage(){
        return image;
    }
}

