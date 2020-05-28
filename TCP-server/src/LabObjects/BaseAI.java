package LabObjects;

import Environment.Collect;
import Environment.Habitat;

import javax.swing.*;
import java.util.Vector;

public abstract class BaseAI {
    protected Thread thread;
    protected boolean workAI=true;
    protected double velocity=5;
    abstract public void move();
    public void setPriority(int priority){thread.setPriority(priority);}
    protected JFrame frame;
}
