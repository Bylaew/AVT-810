import java.awt.*;

public abstract class House implements IBehaviour //все домики
{
    public int x; //координаты
    public int y;
    public abstract Image getImage();

    @Override
    public void setX(int x) {
        this.x = x;
    }
    @Override
    public void setY(int y) {
        this.y = y;
    }
    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
}
