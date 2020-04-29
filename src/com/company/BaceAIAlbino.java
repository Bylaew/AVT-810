package com.company;
//2.	Альбиносы двигаются по оси X от одного края области обитания до другого со скоростью V.
public class BaceAIAlbino extends AbstractBaceAI {

    BaceAIAlbino(Habitat h)
    {
        super(h);
        singleton=Singleton.getInstance();
        V=10;
    }
    @Override
    public void run() {
        while (going) {
            synchronized (this) {
                synchronized (singleton.GetVector()) {
                    for (AbstractRabbit rabbit : singleton.GetVector()) {
                        if (rabbit.getID() < 0) {

                            int x = (int) (rabbit.getX() + rabbit.getDirX() * V);
                            if ((x + 50) >= habitat.getPanelWith() || x <= 0) {
                                rabbit.SetDir(-rabbit.getDirX(), 0);
                            }
                            rabbit.setCoordinates((int) (rabbit.getX() + rabbit.getDirX() * V), rabbit.getY());
                        }
                    }
                }
                if (!habitat.readyAlbino) {
                    try {
                        System.out.println("pirivet3");
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            habitat.frame.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    stopped();
                }

        }
    }

    @Override
    public void stopped() {
        going=false;
    }
}
