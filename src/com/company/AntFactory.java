package com.company;

public class AntFactory implements AbstractFactory {

    @Override
    public Warrior createWarrior() {
        return new Warrior();
    }

    @Override
    public Warrior createWarrior(float x, float y) {
        return new Warrior(x,y);
    }

    @Override
    public Worker createWorker() {
        return new Worker();
    }

    @Override
    public Worker createWorker(float x, float y) {
        return new Worker(x,y);
    }
}
