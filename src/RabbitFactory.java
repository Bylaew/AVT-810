
public class RabbitFactory implements AbstractFactory
{
    @Override
    public Ordinary_Rabbit createOrdinary()
    {
        return new Ordinary_Rabbit();
    }

    @Override
    public Ordinary_Rabbit createOrdinary(int x, int y, long time)
    {
        return new Ordinary_Rabbit(x, y, time);
    }

    @Override
    public Albino createAlbino()
    {
        return new Albino();
    }

    @Override
    public Albino createAlbino(int x, int y, long time)
    {
        return new Albino(x, y, time);
    }
}
