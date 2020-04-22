
public class AlbinoAI extends BaseAI
{
    int speed = 6;

    public AlbinoAI(Habitat h)
    {
        habitat = h;
    }

    public void run()
    {
        while (going)
        {
            for (int i = 0; i < habitat.count; i++)
            {
                if (habitat.rabbitVector.getRabbit(i).type == 'a')
                {
                    Albino rabbit = (Albino) habitat.rabbitVector.getRabbit(i);
                    checkBounds(rabbit);
                    int x = rabbit.getX();
                    if (rabbit.orientation == 0)
                        x -= speed;
                    else x += speed;
                    rabbit.setX(x);
                }
            }
            habitat.draw(habitat.field.getGraphics(), habitat.field);
        }
    }
}
