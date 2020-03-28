package LabObjects;

import java.util.ArrayList;

public interface AbstractFactory {
    House createWood(int height, int width);
    House createKap(int height, int width);
}
