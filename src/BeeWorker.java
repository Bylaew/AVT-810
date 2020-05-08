import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BeeWorker extends Bee {
    private static BufferedImage img;

    BeeWorker() {

    }

    BeeWorker(float x, float y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("WorkerBee.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image getImage() {
        return img;
    }
}