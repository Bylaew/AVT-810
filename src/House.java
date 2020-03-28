import java.awt.*;

public abstract class House //все домики
{
    int x; //координаты
    int y;
    public abstract Image getImage();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
