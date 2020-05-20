import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Rabbit implements IBehaviour, Serializable
{
    int x;
    int y;
    long birthtime;
    int ID;
    enum Orientation { LEFT, RIGHT, BOTTOM, TOP, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT }
    Orientation orientation;
    char type;

    public abstract BufferedImage getImage();
    public abstract void setBirthtime(long time);
    public abstract void setID(int id);
    public abstract long getLifetime();
}
