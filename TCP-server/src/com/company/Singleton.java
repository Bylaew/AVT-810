package com.company;

import java.util.HashMap;

public class Singleton {
    private static Singleton instance;
    HashMap<Integer,ServerConnection> map=new HashMap<>();

    private Singleton(){}

    public HashMap<Integer,ServerConnection> getMap()
    {
        return map;
    }

    public static Singleton getInstance(){
        if(instance==null)
            instance=new Singleton();
        return instance;
    }

}
