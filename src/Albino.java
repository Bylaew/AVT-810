import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Albino extends Rabbit
{
    private Image image;
    static int count = 0;

    public Albino()
    {
        setX(0);
        setY(0);

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

    public Albino(float x, float y)
    {
        setX(x);
        setY(y);

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
