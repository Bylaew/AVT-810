public class WoodFactory implements AbstractFactory
{
    @Override
    public House create() {
        return new Wood();
    }
    @Override
    public House create(int x, int y) {
        return new Wood(x,y);
    }
}
