import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ordinary_Rabbit extends Rabbit
{
    private static BufferedImage image;
    static int count = 0;
    static long lifetime = 20000;

    public Ordinary_Rabbit()
    {
        setX(0);
        setY(0);
        setBirthtime(0);
        setID(0);

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

    public Ordinary_Rabbit(float x, float y, long time)
    {
        setX(x);
        setY(y);
        setBirthtime(time);
        setID((int)(Math.random()*1000));

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
    public void setBirthtime(long time) { birthtime = time; }
    @Override
    public void setID(int id) { ID = id; }
    @Override
    public long getLifetime() { return lifetime; }
    @Override
    public void setX(float x) {this.x = x;}
    @Override
    public void setY(float y) {this.y = y;}
    @Override
    public float getX() {return x;}
    @Override
    public float getY() {return y;}
    @Override
    public void die()
    {
        count--;
    }
}
