public interface AbstractFactory
{
    Ordinary_Rabbit createOrdinary();
    Ordinary_Rabbit createOrdinary(float x, float y, long time);

    Albino createAlbino();
    Albino createAlbino(float x, float y, long time);
}
