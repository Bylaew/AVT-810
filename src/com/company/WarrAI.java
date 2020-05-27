package com.company;

public class WarrAI extends BaseAI {
    int speed = 4;

    boolean isStopped = false;

    public void stopAnimation() throws InterruptedException {
        isStopped = true;

    }

    public synchronized void resumeAnimation() {
        this.notify();
        isStopped = false;
    }

    @Override
    public void run() {
        super.run();
        while (isGoing) {
            synchronized (this) {
                while (isStopped) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < habitat.getMas().getArray().size(); i++) {
                    Ant currentAnt = habitat.getMas().getArray().get(i);
                    if (currentAnt instanceof Warrior) {
                        if (currentAnt.getX() > habitat.mainPanel.getWidth() / 2)
                            currentAnt.setOrientationX(1);
                        else
                            currentAnt.setOrientationX(-1);

                        if (currentAnt.getY() > habitat.mainPanel.getHeight() / 2)
                            currentAnt.setOrientationY(1);
                        else
                            currentAnt.setOrientationY(-1);

                        if (!((Warrior) currentAnt).getStopX() || !((Warrior) currentAnt).getStopY()) {
                            if (currentAnt.getX() + speed * currentAnt.getOrientationY() < 0 || currentAnt.getX() + 100 > habitat.mainPanel.getWidth())
                                ((Warrior) currentAnt).setStopX(true);
                            else
                                currentAnt.setX(currentAnt.getX() + speed * currentAnt.getOrientationX());
                            if (currentAnt.getY() + speed * currentAnt.getOrientationY() < 0 || currentAnt.getY() + 200 > habitat.mainPanel.getHeight())
                                ((Warrior) currentAnt).setStopY(true);
                            else
                                currentAnt.setY(currentAnt.getY() + speed * currentAnt.getOrientationY());
                        } else {
                            if (currentAnt.getX() == ((Warrior) currentAnt).getOldX()) ;
                            else
                                currentAnt.setX(currentAnt.getX() - speed * currentAnt.getOrientationX());
                            if (currentAnt.getY() == ((Warrior) currentAnt).getOldY()) ;
                            else
                                currentAnt.setY(currentAnt.getY() - speed * currentAnt.getOrientationY());

                        }
                    }
                }
                habitat.draw(habitat.mainPanel);
                try {
                    this.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

