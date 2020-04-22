
public class OrdinaryAI extends BaseAI
{
    int speed = 4;
    long N = 5000;

    public OrdinaryAI(Habitat h)
    {
        habitat = h;
    }

    public void run()
    {
        while (going)
        {
            for (int i = 0; i < habitat.count; i++)
            {
                if (habitat.rabbitVector.getRabbit(i).type == 'o')
                {
                    Ordinary_Rabbit rabbit = (Ordinary_Rabbit) habitat.rabbitVector.getRabbit(i);
                    checkBounds(rabbit);
                    int x, y;
                    x = rabbit.getX();
                    y = rabbit.getY();
                    if (rabbit.orientation == 0)
                        x -= speed;
                    else if (rabbit.orientation == 1)
                        x += speed;
                    else if (rabbit.orientation == 2)
                        y += speed;
                    else if (rabbit.orientation == 3)
                        y -= speed;
                    else if (rabbit.orientation == 4)
                    {
                        x += speed;
                        y -= speed;
                    }
                    else if (rabbit.orientation == 5)
                    {
                        x += speed;
                        y += speed;
                    }
                    else if (rabbit.orientation == 6)
                    {
                        x -= speed;
                        y += speed;
                    }
                    else
                    {
                        x -= speed;
                        y -= speed;
                    }
                    rabbit.setX(x);
                    rabbit.setY(y);
                    if (habitat.time % N == 0)
                        rabbit.setOrientation((int)(Math.random()*8));
                }
            }
            habitat.draw(habitat.field.getGraphics(), habitat.field);
        }
    }
}
