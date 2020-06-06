package objects;
import singleton.Packet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.PrivilegedAction;

public class Wood extends House //деревянные домики
{
    public static int counter;
    transient private static Image image;
    private static final long serialVersionUID=4L;
    public Wood()
    {
     setX(0);
     setY(0);
        // получаем изображения
        try
        {
            image = ImageIO.read(new File("wood.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        counter++;
    }
    public Wood(double x, double y)
    {
        setX(x);
        setY(y);

        try
        {
            image = ImageIO.read(new File("wood.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        counter++;
    }
    public Wood(Packet packet)
    {
        this.appTime = packet.appTime;
        this.id = packet.id;
        this.x = packet.x;
        this.y = packet.y;
        this.lifePeriod= packet.lifePeriod;
        this.vy = packet.vy;
        this.vx = packet.vx;
        this.tact = packet.tact;
        this.lasttact = packet.lasttact;
        counter++;
        try
        {
            image = ImageIO.read(new File("wood.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public Image getImage() {
        {
            // получаем изображения
            try
            {
                image = ImageIO.read(new File("wood.png"));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return image;
        }
    }
}
