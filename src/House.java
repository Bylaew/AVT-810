import java.awt.*;

public abstract class House implements IBehaviour //все домики
{
    public int x; //координаты
    public int y;
    public abstract Image getImage();
    public int lifePeriod;
    public int appTime;
    public int id;

    public void setId(int id) {
        this.id = id;
    }
    public void setLifePeriod(int lifePeriod)
    {
        this.lifePeriod=lifePeriod;
    }
    public void setAppTime(int appTime)
    {
        this.appTime=appTime;
    }
    @Override
    public void setX(int x) {
        this.x = x;
    }
    @Override
    public void setY(int y) {
        this.y = y;
    }
    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }

    public int getAppTime() {
        return appTime;
    }

    public int getLifePeriod() {
        return lifePeriod;
    }
    public int getId() {
        return id;
    }
}
