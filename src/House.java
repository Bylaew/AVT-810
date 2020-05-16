import java.awt.*;
import java.io.Serializable;

public abstract class House implements IBehaviour, Serializable //все домики
{
    public double x; //координаты
    public double y;
    public abstract Image getImage();
    public int lifePeriod;
    public int appTime;
    public int id;
    public double vx=0;
    public double vy=0;
    private int tact=0;
    private int lasttact=0;

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
    public void setX(double x) {
        this.x = x;
    }
    @Override
    public void setY(double y) {
        this.y = y;
    }
    @Override
    public double getX() {
        return x;
    }
    @Override
    public double getY() {
        return y;
    }

    public int getTact() {
        return tact;
    }

    public int getLasttact() {
        return lasttact;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        if (this.vx==0){
        this.vx = vx;}
    }

    public void setVy(double vy) {
        if (this.vy==0){
        this.vy = vy;}
    }

    public void setTact(int tact) {
        this.tact = tact;
    }
    public void setLasttact(int l)
    {
        if (this.lasttact==0){
        this.lasttact = l;}
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
