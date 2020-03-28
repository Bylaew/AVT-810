public class StoneFactory implements AbstractFactory
{
    @Override
    public House create() {
        return new Stone();
    }

    @Override
    public House create(int x, int y) {
        return new Stone(x,y);
    }
}
