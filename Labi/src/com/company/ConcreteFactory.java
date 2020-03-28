package com.company;

public class ConcreteFactory implements abstractFactory {


    @Override
    public Rabbits createRabbit() {
        //System.out.println("fir fir fir");
        return new Rabbits((int)(Math.random()*500),(int)(Math.random()*500));
    }

    @Override
    public albinoRabbit createAlbinoRabbit() {
        //System.out.println("firrrrr firrrrr firrrrr");
        return new albinoRabbit((int)(Math.random()*500),(int)(Math.random()*500));
    }
}
