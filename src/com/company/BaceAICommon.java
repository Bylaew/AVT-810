package com.company;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//1.	Обыкновенные кролики двигаются хаотично со скоростью V. Хаотичность достигается случайной сменой направления движения раз в N секунд.
public class BaceAICommon extends AbstractBaceAI {
    int N=500;
    boolean bool =false;
    BaceAICommon(){}

    BaceAICommon(Habitat h) {
        super(h);
        singleton=Singleton.getInstance();
        V=10;
    }

    @Override
    public void run() {
        while (going) {
            synchronized (singleton.GetVector()) {
                if (!habitat.ready) {
                    try {
                        System.out.println("pirivet2");
                        singleton.GetVector().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (AbstractRabbit rabbit : singleton.GetVector()) {
                    if (rabbit.getID() > 0) {

                                rabbit.setCoordinates((int) (rabbit.getX() + rabbit.getDirX() * V), (int) (rabbit.getY() - rabbit.getDirY() * V));
                                if ((habitat.currentTime - rabbit.getTimeBirth()) % N == 0) {
                                    rabbit.SetDir(Math.random() * 2 - 1, Math.random() * 2 - 1);
                                }
                    }
                }
            }
            try {
                Thread.sleep(200);
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
