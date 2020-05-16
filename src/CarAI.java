public class CarAI extends BaseAI {
    Habitat h;

    CarAI(Habitat habitat)
    {
        h=habitat;
    }

    public void run() {
        float speed = 8;
        while (true) {
            if (working) {
                for (int i = 0; i < h.singleton.vehicles.size() - 1; i++) {
                    if (h.singleton.vehicles.get(i) instanceof Car) {
                        float x1 = h.singleton.vehicles.get(i).getX();
                        float x2 = x1 - speed;
                        if(x2 <-150) x2 = h.window.getWidth()+150;
                        h.singleton.vehicles.get(i).setX(x2);
                        System.out.println("Car " + i + " Moved");
                    }
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void pauseAI()
    {
        working=false;
    }
    public synchronized void resumeAI()
    {
        this.notify();
        working = true;
    }
}

