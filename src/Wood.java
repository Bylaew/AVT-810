import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Wood extends House //деревянные домики
{
    static int counter;
    transient private Image image;
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
