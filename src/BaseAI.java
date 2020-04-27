
public abstract class BaseAI extends Thread
{
    boolean going = false;
    Habitat habitat;

    void checkBounds(Rabbit rabbit)
    {
        int x, y;
        x = rabbit.getX();
        y = rabbit.getY();
        if (x + rabbit.getImage().getWidth() >= habitat.field.getWidth())
            rabbit.orientation = Rabbit.Orientation.LEFT;
        if (x <= 0)
            rabbit.orientation = Rabbit.Orientation.RIGHT;
        if (y + rabbit.getImage().getHeight() >= habitat.field.getHeight())
            rabbit.orientation = Rabbit.Orientation.TOP;
        if (y <= 0)
            rabbit.orientation = Rabbit.Orientation.BOTTOM;
        if ((x <= 0) && (y + rabbit.getImage().getHeight() >= habitat.field.getHeight()))
            rabbit.orientation = Rabbit.Orientation.TOP_RIGHT;
        if ((x <= 0) && (y <= 0))
            rabbit.orientation = Rabbit.Orientation.BOTTOM_RIGHT;
        if ((x + rabbit.getImage().getWidth() >= habitat.field.getWidth()) && (y <= 0))
            rabbit.orientation = Rabbit.Orientation.BOTTOM_LEFT;
        if ((x + rabbit.getImage().getWidth() >= habitat.field.getWidth()) && (y + rabbit.getImage().getHeight() >= habitat.field.getHeight()))
            rabbit.orientation = Rabbit.Orientation.TOP_LEFT;
    }
}
