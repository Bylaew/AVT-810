public interface AbstractFactory {
    BeeMale createBeeMale();

    BeeWorker createBeeWorker();

    BeeMale createBeeMale(float x, float y);

    BeeWorker createBeeWorker(float x, float y);

}
