import java.awt.image.BufferedImage;

public abstract class Rabbit implements IBehaviour
{
    float x;
    float y;
    long birthtime;
    int ID;

    public abstract BufferedImage getImage();
    public abstract void setBirthtime(long time);
    public abstract void setID(int id);
    public abstract long getLifetime();
}
