package com.company;
import java.lang.Thread;
/*1)	создать абстрактный класс BaseAI, описывающий «интеллектуальное поведение» объектов.
 Класс должен создавать поток, обеспечивающий движения объектов коллекции;*/
public abstract class AbstractBaceAI implements Runnable {//создание потоков
    boolean going=true;
    int V=0;
    Singleton singleton=Singleton.getInstance();
    Habitat habitat;

    AbstractBaceAI(){}

    AbstractBaceAI(Habitat habitat){
        this.habitat=habitat;
    }
    public abstract void run();
    public abstract void stopped();
}
