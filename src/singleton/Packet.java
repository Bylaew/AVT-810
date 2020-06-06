package singleton;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import objects.*;

import java.awt.*;
import java.io.Serializable;

public class Packet implements Serializable
{
    public int type;//0- wood 1 - stone

    public double x; //координаты
    public double y;
    public int lifePeriod;
    public int appTime;
    public int id;
    public double vx=0;
    public double vy=0;
    public int tact=0;
    public int lasttact=0;
    private static final long serialVersionUID=8L;

    public Packet (House house)
    {
        if (house instanceof  Wood)
            this.type = 0;
        if (house instanceof Stone)
            this.type = 1;
        this.x  = house.x;
        this.y = house .y;
        this.lifePeriod = house.lifePeriod;
        this.appTime = house.appTime;
        this.id = house.id;
        this.vx = house.vx;
        this.vy = house.vy;
        this.tact= house.getTact();
        this.lasttact = house.getLasttact();


    }
}
