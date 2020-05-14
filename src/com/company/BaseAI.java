package com.company;

public abstract class BaseAI extends Thread {
    Habitat habitat;
    boolean isGoing;

    public void setGoing(boolean going) {
        isGoing = going;
    }



    public Habitat getHabitat() {
        return habitat;
    }
    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }


}
