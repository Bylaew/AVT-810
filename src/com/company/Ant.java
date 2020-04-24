package com.company;

public abstract class Ant implements IBehaviour {
    private float x;
    private float y;

    private int indef;
    private int bornTime;
    private int lifeTime;
    Ant(){
        x=0;
        y=0;
    }
    Ant(float x, float y, int bornTime, int lifeTime, int indef){
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

    public int getIndef() {
        return indef;
    }

    public void setIndef(int indef) {
        this.indef = indef;
    }

    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }


}
