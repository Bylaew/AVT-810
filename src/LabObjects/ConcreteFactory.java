package LabObjects;

import java.util.ArrayList;

public class ConcreteFactory implements AbstractFactory {
    @Override
    public House createWood(int height, int width, int ID){
        House wood = new Wooden_house(height, width, ID);
        return wood;
    };
    @Override
    public House createKap(int height, int width, int ID){
        House ussr = new Kap_House(height, width, ID);
        return ussr;
    };
}
