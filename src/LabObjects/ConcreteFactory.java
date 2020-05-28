package LabObjects;

import java.util.ArrayList;

public class ConcreteFactory implements AbstractFactory {
    @Override
    public House createWood(int height, int width){
        House wood = new Wooden_house(height, width);
        return wood;
    }
    @Override
    public House createKap(int height, int width){
        House ussr = new Kap_House(height, width);
        return ussr;
    }
}
