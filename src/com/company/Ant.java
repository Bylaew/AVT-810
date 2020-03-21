package com.company;

public abstract class Ant implements IBehaviour {
    private float x;
    private float y;

    Ant(){
        x=0;
        y=0;
    }
    Ant(float x, float y){
        this.x = x;
        this.y = y;
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
