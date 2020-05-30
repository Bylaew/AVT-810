public class MotoAI extends BaseAI {
    Habitat h;

        MotoAI(Habitat habitat)
        {
            h=habitat;
        }

    public synchronized void run() {
        float speed = 5;
        while (true){
        while (working) {
            for (Vehicle current : h.singleton.vehicles) {
                if (current instanceof Moto) {
                    float y1 = current.getY();
                    float y2 = y1 - speed;
                    if (y2 < -50) y2 = h.window.getHeight();
                    current.setY(y2);
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
