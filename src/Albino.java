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
        setOrientation(Orientation.values()[1]);
        setBirthtime(0);
        setID(0);
        type = 'a';

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

    public Albino(int x, int y, long time)
    {
        setX(x);
        setY(y);
        setOrientation(Orientation.values()[(int)(Math.random()*2)]);
        setBirthtime(time);
        setID((int)(Math.random()*1000));
        type = 'a';

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
    public void setX(int x) {this.x = x;}
    @Override
    public void setY(int y) {this.y = y;}
    @Override
    public void setOrientation(Orientation orientation) { this.orientation = orientation; }
    @Override
    public int getX() {return x;}
    @Override
    public int getY() {return y;}
    @Override
    public void die()
    {
        count--;
    }
}
