package com.company;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

public class Singleton {
    Vector<AbstractRabbit> arr=new Vector();
    private static Singleton instance;
    public TreeSet ID=new TreeSet();
    HashMap map=new HashMap();
    private Singleton(){}

    public Vector<AbstractRabbit> GetVector()
    {
     return arr;
    }

    public void refreshVector() {
        arr.clear();
    }

    public TreeSet getID(){
        return ID;
    }

    public void refreshID() {
        ID.clear();
    }

    public HashMap GetMap() {
        return map;
    }

    public void refreshMap(){
        map.clear();
    }

    public static Singleton getInstance()
    {
        if(instance==null)
            instance=new Singleton();
        return instance;
    }
}
