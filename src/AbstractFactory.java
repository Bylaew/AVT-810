public interface AbstractFactory
{
    Ordinary_Rabbit createOrdinary();
    Ordinary_Rabbit createOrdinary(float x, float y);

    Albino createAlbino();
    Albino createAlbino(float x, float y);
}
