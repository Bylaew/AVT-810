package com.company;

public class WorkAI extends BaseAI {
    int speed = 6;


    @Override
    public void run() {
        super.run();
        System.out.println("123");
        while (isGoing) {
            for (int i = 0; i < habitat.getMas().getArray().size(); i++) {
                Ant currentAnt = habitat.getMas().getArray().get(i);
                if (currentAnt instanceof Worker) {

                        currentAnt.setX((float)(currentAnt.getX() + habitat.R * Math.cos((habitat.getTimeFromStart()*speed))));
                        currentAnt.setY((float)(currentAnt.getY() + habitat.R * Math.sin((habitat.getTimeFromStart()*speed))));

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
