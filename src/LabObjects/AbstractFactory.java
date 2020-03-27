package LabObjects;

import java.util.ArrayList;

public interface AbstractFactory {
    void createWood(ArrayList<House> container, int height, int width);
    void createKap(ArrayList<House> container, int height, int width);
}
