import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Albino extends Rabbit
{
    private static BufferedImage image;
    static int count = 0;
    static long lifetime = 15000;

    public Albino()
    {
        setX(0);
        setY(0);
        setBirthtime(0);
        setID(0);

        try
        {
            image = ImageIO.read(new File("./src/Albino.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        count++;
    }

    public Albino(float x, float y, long time)
    {
        setX(x);
        setY(y);
        setBirthtime(time);
        setID((int)(Math.random()*1000));

        try
        {
            image = ImageIO.read(new File("./src/Albino.png"));
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
