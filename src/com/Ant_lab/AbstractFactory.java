package com.Ant_lab;

public interface AbstractFactory
{
    Ant_war createWar();
    Ant_war createWar(float x, float y);
    Ant_work createWork();
    Ant_work createWork(float x, float y);
}
