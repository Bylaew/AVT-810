package com.company;

public abstract class Person implements IBehaviour{
    private float x;
    private float y;

    Person(){
        x=0;
        y=0;
    }
    Person(float x, float y){
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
