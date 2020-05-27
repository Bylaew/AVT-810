package com.company;

import java.io.Serializable;

public abstract class AbstractRabbit implements InBehavior, Serializable {

    int x,y;
    long BirthTime;
    int ID;
    double dirX=1, dirY=1;

    protected AbstractRabbit() {
    }
    abstract void setCoordinates(int x,int y);
    abstract long getTimeBirth();

    abstract int getID();

    void SetDir(double newX, double newY)
    {
        this.dirX = newX;
        this.dirY = newY;
    }

    public String toString(){
        return "rabbit: "+this.ID;
    }

    public double getDirX() { return dirX; }
    public double getDirY() { return dirY; }

    AbstractRabbit(int x,int y,long Birth,int ID)
    {
        this.x=x;
        this.y=y;
        this.BirthTime=Birth;
        this.ID=ID;
    }
}
