package com.company;

public interface AbstractFactory {
    Warrior createWarrior();
    Warrior createWarrior(float x, float y, int lifeTime, int bornTime,int ingef);
    Worker createWorker();
    Worker createWorker(float x, float y,int lifeTime, int bornTime,int ingef);
}
