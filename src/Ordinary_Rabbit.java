import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ordinary_Rabbit extends Rabbit
{
    private Image image;
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
    public Image getImage()
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
