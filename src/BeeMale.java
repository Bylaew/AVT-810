import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BeeMale extends Bee {
    private static BufferedImage img;

    BeeMale() {

    }

    BeeMale(float x, float y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("MaleBee.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image getImage() {
        return img;
    }

}