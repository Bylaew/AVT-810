package com.company;

import java.io.Serializable;

public abstract class Ant implements IBehaviour, Serializable {
    protected int x;
    protected int y;
    protected int orientationX;
    protected int orientationY;

    protected int indef;
    protected int bornTime;
    protected int lifeTime;

    Ant() {
        x = 0;
        y = 0;
    }

    Ant(int x, int y, int bornTime, int lifeTime, int indef) {
        this.x = x;
        this.y = y;
        this.bornTime = bornTime;
        this.lifeTime = lifeTime;
        this.indef = indef;
    }

    public void setBornTime(int bornTime) {
        this.bornTime = bornTime;
    }

    public int getBornTime() {
        return bornTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public int getOrientationX() {
        return orientationX;
    }

    public void setOrientationX(int orientationX) {
        this.orientationX = orientationX;
    }

    public int getOrientationY() {
        return orientationY;
    }

    public void setOrientationY(int orientationY) {
        this.orientationY = orientationY;
    }

    public int getIndef() {
        return indef;
    }

    public void setIndef(int indef) {
        this.indef = indef;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
