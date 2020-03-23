package com.company;

public interface AbstractFactory {
    Warrior createWarrior();
    Warrior createWarrior(float x, float y);
    Worker createWorker();
    Worker createWorker(float x, float y);
}
