import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Car extends Vehicle {
    public static int count;
    private static BufferedImage image;
    static {try {
        image = ImageIO.read(new File("Car.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    Car(float x, float y, int id) {
        super(x, y, id);
        count++;
    }

    public Image getImage(){
        return image;
    }
}


