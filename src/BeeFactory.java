public class BeeFactory implements AbstractFactory {

    @Override
    public BeeMale createBeeMale() {
        return new BeeMale();
    }

    @Override
    public BeeWorker createBeeWorker() {
        return new BeeWorker();
    }

    @Override
    public BeeMale createBeeMale(float x, float y) {
        return new BeeMale(x, y);
    }

    @Override
    public BeeWorker createBeeWorker(float x, float y) {
        return new BeeWorker(x, y);
    }

}
