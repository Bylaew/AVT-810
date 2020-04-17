package com.company;

public abstract class AbstractRabbit implements InBehavior {

    int x,y;
    long BirthTime;
    int ID;

    protected AbstractRabbit() { }

    abstract long getTimeBirth();

    abstract int getID();

    abstract void go();
    abstract void stay();

    AbstractRabbit(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
}
