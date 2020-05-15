package com.company;

public class WorkAI extends BaseAI {
    int speed = 6;
    int r = 4;
    long time = 0;

    boolean isStopped = false;

    public void stopAnimation() throws InterruptedException {
        isStopped = true;

    }

    public synchronized void resumeAnimation() {
        this.notify();
        isStopped=false;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("123");
        while (isGoing) {
            synchronized (this) {
                if (isStopped) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    time++;
                    for (int i = 0; i < habitat.getMas().getArray().size(); i++) {
                        Ant currentAnt = habitat.getMas().getArray().get(i);
                        if (currentAnt instanceof Worker) {
                            currentAnt.setX((float) (currentAnt.getX() + r * Math.cos((time * speed / 100))));
                            currentAnt.setY((float) (currentAnt.getY() + r * Math.sin((time * speed / 100))));

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
}
