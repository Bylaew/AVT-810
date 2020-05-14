package com.company;

public class WarrAI extends BaseAI {
    int speed = 6;

    @Override
    public void run() {
        super.run();
        System.out.println("123");
        while (isGoing) {
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

                    if (currentAnt.getX() + speed * currentAnt.getOrientationY() < 0 || currentAnt.getX() + 100 > habitat.mainPanel.getWidth())
                        ;
                    else
                        currentAnt.setX(currentAnt.getX() + speed * currentAnt.getOrientationX());

                    if (currentAnt.getY() + speed * currentAnt.getOrientationY() < 0 || currentAnt.getY() + 200 > habitat.mainPanel.getHeight())
                        ;
                    else
                        currentAnt.setY(currentAnt.getY() + speed * currentAnt.getOrientationY());

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
