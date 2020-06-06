package factory;
import objects.*;
public class ConcreteFactory implements AbstractFactory
{
    @Override
    public House createStone(int x, int y) {
        return new Stone(x,y);
    }

    @Override
    public House createWood(int x, int y) {
        return new Wood(x,y);
    }
}
