package com.company;

public abstract class AbstractRabbit implements InBehavior {

    private int x,y;

    protected AbstractRabbit() {
    }

    abstract void go();
    abstract void stay();

    AbstractRabbit(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

}
