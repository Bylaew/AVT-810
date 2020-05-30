package com.company;

public class PersonFactory implements AbstractFactory{

    @Override
    public Fiz_L createFiz() {
        return new Fiz_L();
    }

    @Override
    public Fiz_L createFiz(float x, float y) {
        return new Fiz_L(x,y);
    }

    @Override
    public Yur_L createYur() {
        return new Yur_L();
    }

    @Override
    public Yur_L createYur(float x, float y) {
        return new Yur_L(x,y);
    }
}
