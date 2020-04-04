package com.Ant_lab;

public abstract class Ant implements IBehaviour
{
    protected float x;
    protected float y;

    public Ant()
    {
     x = 0;
     y = 0;
    }

    public Ant(float x,float y)
    {
      this.x = x;
      this.y = y;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getX(float x)
    {
        return x;
    }

    public float getY(float y)
    {
        return y;
    }

}
