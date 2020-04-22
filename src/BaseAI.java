
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
            rabbit.orientation = 0; // влево
        if (x <= 0)
            rabbit.orientation = 1; // вправо
        if (y + rabbit.getImage().getHeight() >= habitat.field.getHeight())
            rabbit.orientation = 3; // вверх
        if (y <= 0)
            rabbit.orientation = 2; // вниз
        if ((x <= 0) && (y + rabbit.getImage().getHeight() >= habitat.field.getHeight()))
            rabbit.orientation = 4; // вверх вправо
        if ((x <= 0) && (y <= 0))
            rabbit.orientation = 5; // вниз вправо
        if ((x + rabbit.getImage().getWidth() >= habitat.field.getWidth()) && (y <= 0))
            rabbit.orientation = 6; // вниз влево
        if ((x + rabbit.getImage().getWidth() >= habitat.field.getWidth()) && (y + rabbit.getImage().getHeight() >= habitat.field.getHeight()))
            rabbit.orientation = 7; // вверх влево
    }
}

/*  Orientation:
            3
        7       4
    0               1
        6       5
            2
 */
