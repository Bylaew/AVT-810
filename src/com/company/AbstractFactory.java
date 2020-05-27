package com.company;

public interface AbstractFactory {
    Warrior createWarrior(int x, int y, int lifeTime, int bornTime,int ingef);
    Worker createWorker(int x, int y,int lifeTime, int bornTime,int ingef);
}
