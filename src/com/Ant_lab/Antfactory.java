package com.Ant_lab;

public class Antfactory implements AbstractFactory
{
    @Override
    public Ant_war createWar() {
        return new Ant_war();
    }

    @Override
    public Ant_war createWar(float x, float y) {
        return new Ant_war(x,y);
    }

    @Override
    public Ant_work createWork() {
        return new Ant_work();
    }

    @Override
    public Ant_work createWork(float x, float y) {
        return new Ant_work(x,y);
    }

}
