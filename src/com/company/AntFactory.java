package com.company;

public class AntFactory implements AbstractFactory {


    @Override
    public Warrior createWarrior(int x, int y,int lifeTime, int bornTime,int ingef) {
        return new Warrior(x,y,bornTime,lifeTime,ingef);
    }

    @Override
    public Worker createWorker(int x, int y,int lifeTime, int bornTime,int ingef) {
        return new Worker(x,y,bornTime,lifeTime,ingef);
    }
}
