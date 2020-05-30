public class CarAI extends BaseAI {
    Habitat h;

    CarAI(Habitat habitat)
    {
        h=habitat;
    }

    public synchronized void run() {
        float speed = 6;
        while (true){
            while (working) {
                for (Vehicle current : h.singleton.vehicles) {
                    if (current instanceof Car) {
                        float x1 = current.getX();
                        float x2 = x1 - speed;
                        if (x2 < -150) x2 = h.window.getWidth();
                        current.setX(x2);
                    }
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } }
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

