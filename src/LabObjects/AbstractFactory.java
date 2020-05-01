package LabObjects;

import java.util.ArrayList;

public interface AbstractFactory {
    House createWood(int height, int width, int ID);
    House createKap(int height, int width, int ID);
}
