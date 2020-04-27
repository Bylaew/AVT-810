
public class AlbinoAI extends BaseAI
{
    int speed = 6;
    boolean isStopped = false;

    public AlbinoAI(Habitat h)
    {
        habitat = h;
    }

    public void run()
    {
        while (going)
        {
            synchronized (this)
            {
                if (isStopped)
                {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    for (int i = 0; i < habitat.count; i++)
                    {
                        if (habitat.rabbitVector.getRabbit(i).type == 'a')
                        {
                            Albino rabbit = (Albino) habitat.rabbitVector.getRabbit(i);
                            checkBounds(rabbit);
                            int x = rabbit.getX();
                            if (rabbit.orientation == Rabbit.Orientation.LEFT)
                                x -= speed;
                            else x += speed;
                            rabbit.setX(x);
                        }
                    }
                    habitat.draw(habitat.field.getGraphics(), habitat.field);
                }
            }
        }
    }

    public void stopAnimation()
    {
        isStopped = true;
    }
    public void resumeAnimation()
    {
        isStopped = false;
        this.notify();
    }
}
