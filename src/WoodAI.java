public class WoodAI extends BaseAI
{
    Habitat h;
    WoodAI(Habitat habitat)
    {
        h=habitat;
    }
    public  void run() {
        double speed = 1000;
        //System.out.println(h.houseSingleton.houseList.get(0).x);
        //System.out.println(h.houseSingleton.houseList.get(0).y);
        while (true) {
            if (working) {
                for (int i = 0; i < h.houseSingleton.houseList.size() - 1; i++) {

                    if (h.houseSingleton.houseList.get(i) instanceof Wood) {
                        double x1 = h.houseSingleton.houseList.get(i).x;
                        double y1 = h.houseSingleton.houseList.get(i).y;
                        double x2 = (h.ui.simulation_panel.getWidth() / 2) * Math.random() + h.ui.simulation_panel.getWidth() / 2;
                        double y2 = (h.ui.simulation_panel.getHeight() / 2) * Math.random() + h.ui.simulation_panel.getHeight() / 2;

                        double a = x2 - x1;
                        double b = y2 - y1;
                        double c = a * a + b * b;
                        double vx = (speed * a) / c;
                        h.houseSingleton.houseList.get(i).setVx(vx);//System.out.println(vx);
                        double vy = (speed * b) / c;
                        h.houseSingleton.houseList.get(i).setVy(vy);//System.out.println(vy);
                        int tact = h.houseSingleton.houseList.get(i).getTact();
                        int lasttact = (int) (c / speed);
                        h.houseSingleton.houseList.get(i).setLasttact(lasttact);
                        //System.out.println(h.houseSingleton.houseList.get(i).getTact());
                        //System.out.println(h.houseSingleton.houseList.get(i).getLasttact());
                        if (h.houseSingleton.houseList.get(i).getTact() < h.houseSingleton.houseList.get(i).getLasttact()) {
                            tact++;
                            h.houseSingleton.houseList.get(i).setTact(tact);
                            x1 = x1 + h.houseSingleton.houseList.get(i).getVx();
                            y1 = y1 + h.houseSingleton.houseList.get(i).getVy();

                            h.houseSingleton.houseList.get(i).setX(x1);
                            h.houseSingleton.houseList.get(i).setY(y1);
                        }
                    }
                }
                try {
                    this.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
           else
            {
                synchronized (this)
                {
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
        working = true;
        this.notify();
    }
}
