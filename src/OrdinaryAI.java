
public class OrdinaryAI extends BaseAI
{
    int speed = 4;
    long N = 5000;
    boolean isStopped = false;
    long prevtime = 0;

    public OrdinaryAI(Habitat h)
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
                        if (habitat.rabbitVector.getRabbit(i).type == 'o')
                        {
                            Ordinary_Rabbit rabbit = (Ordinary_Rabbit) habitat.rabbitVector.getRabbit(i);
                            checkBounds(rabbit);
                            int x, y;
                            x = rabbit.getX();
                            y = rabbit.getY();
                            if (rabbit.orientation == Rabbit.Orientation.LEFT)
                                x -= speed;
                            else if (rabbit.orientation == Rabbit.Orientation.RIGHT)
                                x += speed;
                            else if (rabbit.orientation == Rabbit.Orientation.BOTTOM)
                                y += speed;
                            else if (rabbit.orientation == Rabbit.Orientation.TOP)
                                y -= speed;
                            else if (rabbit.orientation == Rabbit.Orientation.TOP_RIGHT)
                            {
                                x += speed;
                                y -= speed;
                            }
                            else if (rabbit.orientation == Rabbit.Orientation.BOTTOM_RIGHT)
                            {
                                x += speed;
                                y += speed;
                            }
                            else if (rabbit.orientation == Rabbit.Orientation.BOTTOM_LEFT)
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
                            if ((habitat.time % N == 0)&&(habitat.time / N != prevtime))
                                rabbit.setOrientation(Rabbit.Orientation.values()[(int)(Math.random()*8)]);
                        }
                    }
                    prevtime = habitat.time / N;
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
