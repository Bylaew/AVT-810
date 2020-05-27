package com.company;

import java.io.Serializable;
import java.util.Vector;

class Singleton implements Serializable {

    private Vector<Ant> array = null;

    public void setArray(Vector<Ant> arr) {
        array = arr;
    }

    public synchronized Vector<Ant> getArray() {
        return array;
    }

}