public abstract class Vehicle implements IBehavior{
    private float x, y;

    Vehicle(){
        this.x = 0;
        this.y = 0;
    }

    Vehicle(float x, float y){
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
