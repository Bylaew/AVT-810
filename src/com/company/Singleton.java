package com.company;

import java.util.ArrayList;

public class Singleton {
    ArrayList<AbstractRabbit>arr=new ArrayList<AbstractRabbit>();
    private static Singleton instance;

    private Singleton(){}

    public ArrayList<AbstractRabbit> GetArray()
    {
     return arr;
    }

    public void refreshArray(){
        arr=new ArrayList<AbstractRabbit>();
    }

    public static Singleton getInstance()
    {
        if(instance==null)
            instance=new Singleton();
        return instance;
    }
}
