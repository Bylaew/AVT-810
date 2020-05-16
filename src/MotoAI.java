    public class MotoAI extends BaseAI {
        Habitat h;

        MotoAI(Habitat habitat)
        {
            h=habitat;
        }
        public  void run() {
            float speed = 5;
            while (true) {
                if (working) {
                    for (int i = 0; i < h.singleton.vehicles.size() - 1; i++) {
                        if (h.singleton.vehicles.get(i) instanceof Moto) {
                            float y1= h.singleton.vehicles.get(i).getY();
                            float y2 = y1 - speed;
                            if(y2 < -50) y2 = h.window.getWidth();
                            h.singleton.vehicles.get(i).setY(y2);
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
