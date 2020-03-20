public abstract class Bee implements IBehaviour {
    private float x, y;

    Bee() {
        x = 0;
        y = 0;
    }

    Bee(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
