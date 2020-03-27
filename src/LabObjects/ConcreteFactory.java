package LabObjects;

import java.util.ArrayList;

public class ConcreteFactory implements AbstractFactory {
    public void createWood(ArrayList<House> container, int height, int width){
        House wood = new Wooden_house(height, width);
        container.add(wood);
    };
    public void createKap(ArrayList<House> container, int height, int width){
        House ussr = new Kap_House(height, width);
        container.add(ussr);
    };
}
