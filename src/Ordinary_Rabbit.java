import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ordinary_Rabbit extends Rabbit
{
    private static BufferedImage image;
    static int count = 0;

    public Ordinary_Rabbit()
    {
        setX(0);
        setY(0);

        try
        {
            image = ImageIO.read(new File("./src/Ordinary.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        count++;
    }

    public Ordinary_Rabbit(float x, float y)
    {
        setX(x);
        setY(y);

        try
        {
            image = ImageIO.read(new File("./src/Ordinary.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        count++;
    }

    @Override
    public BufferedImage getImage()
    {
        return image;
    }
    @Override
    public void setX(float x) {this.x = x;}
    @Override
    public void setY(float y) {this.y = y;}
    @Override
    public float getX() {return x;}
    @Override
    public float getY() {return y;}
}
