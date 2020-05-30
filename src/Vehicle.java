import java.io.Serializable;

public abstract class Vehicle implements IBehavior, Serializable {

    private float x, y;
    private int id;
    private long birthTime;
    private long lifeTime;

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }

    Vehicle(float x, float y, int id){
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }


    public long getLifeTime(){return lifeTime;}
}
