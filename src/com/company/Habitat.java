package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Habitat {
    private int width;
    private int height;
    private ArrayList<Person> mas;
    private Timer startTime;
    private int fizCount, yurCount;
    private int pos1 =0, pos2 =0;
    private long timeElapsed = 0;
    private float N1, N2, P1, P2;
    private PersonFactory personFactory;
    private boolean firstTimeRun = false;

    Habitat() {
        this.height = 700;
        this.width = 700;
        personFactory = new PersonFactory();
        mas = new ArrayList<Person>();
    }

    public void init(float N1, float N2, float P1, float P2) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.P2 = P2;
        JFrame window = new JFrame("Filing-Cabinet");
        window.setSize(width, height);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocation(400, 100);

        JPanel panel = new JPanel();
        JLabel labelTime = new JLabel();
        labelTime.setText(timeElapsed / 60 + ":" + ((timeElapsed % 60 < 10) ? "0" : "") + timeElapsed % 60);
        labelTime.setFont(new Font("Arial", Font.ITALIC, 40));
        labelTime.setVerticalAlignment(JLabel.TOP);
        labelTime.setHorizontalAlignment(JLabel.RIGHT);
        labelTime.setVisible(false);
        panel.add(labelTime);

        window.add(panel);

        startTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeElapsed++;
                labelTime.setText(timeElapsed / 60 + ":" + ((timeElapsed % 60 < 10) ? "0" : "") + timeElapsed % 60);
                update(timeElapsed, window);
            }
        });

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_B):
                        if (!firstTimeRun) {
                            firstTimeRun = true;
                            start(window);
                        }
                        break;
                    case (KeyEvent.VK_E):
                        startTime.stop();
                        statistic(window);
                        window.repaint();
                        fizCount = 0;
                        yurCount = 0;
                        timeElapsed = 0;
                        pos1 = 0;
                        pos2 = 0;
                        labelTime.setText(timeElapsed / 60 + ":" + ((timeElapsed % 60 < 10) ? "0" : "") + timeElapsed % 60);
                        firstTimeRun = false;
                        mas = null;
                        break;
                    case (KeyEvent.VK_T):
                        if (labelTime.isVisible())
                            labelTime.setVisible(false);
                        else labelTime.setVisible(true);
                        break;
                }
            }
        });
        window.setVisible(true);
    }

    private void start(JFrame window) {
        System.out.println("Start");
        mas = new ArrayList<Person>();
        startTime.start();
    }

    private void statistic(JFrame window) {
        System.out.println("Stats");
        JDialog dialog = new JDialog(window, "Stats");

        JLabel dialogLabel1 = new JLabel("Individuals: " + fizCount);
        JLabel dialogLabel2 = new JLabel("Companies:" + yurCount);
        JLabel dialogLabel3 = new JLabel("Sum of records: " + (fizCount + yurCount));
        JLabel dialogLabel4 = new JLabel("Elapsed Time: " + timeElapsed / 60 + ":" + ((timeElapsed % 60 < 10) ? "0" : "") + timeElapsed % 60);

        dialogLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        dialogLabel1.setForeground(Color.black);

        dialogLabel2.setFont(new Font("Arial", Font.BOLD, 14));
        dialogLabel2.setForeground(Color.black);

        dialogLabel3.setFont(new Font("Arial", Font.BOLD, 14));
        dialogLabel3.setForeground(Color.black);

        dialogLabel4.setFont(new Font("Arial", Font.BOLD, 14));
        dialogLabel4.setForeground(Color.black);

        dialog.setLayout(new GridLayout(4, 1));

        dialog.add(dialogLabel1);
        dialog.add(dialogLabel2);
        dialog.add(dialogLabel3);
        dialog.add(dialogLabel4);

        dialog.setSize(250, 160);

        dialog.setVisible(true);
    }

    private void settings() {
        System.out.println("setting");
    }

    private void update(float time, JFrame window) {
        if (width <= pos1){
            pos1 = 0;
            pos2 +=200;
        }
        if (time % N1 == 0) {
            if (Math.random() < P1) {
                mas.add(personFactory.createFiz( pos1, pos2));
                System.out.println("Fiz Created at (" + mas.get(mas.size()-1).getX() + "," + mas.get(mas.size()-1).getY() + ")");
                for(Person a:mas)
                    window.getGraphics().drawImage(a.getImage(), (int) a.getX(), (int) a.getY(), 100, 200, null);
                fizCount++;
                pos1 +=100;
            }
        }
        if (width <= pos1){
            pos1 = 0;
            pos2 +=200;
        }
        if (time % N2 == 0) {
            if (Math.random() < P2) {
                mas.add(personFactory.createYur(pos1, pos2));

                System.out.println("Yur Created at ("+ mas.get(mas.size()-1).getX() + "," + mas.get(mas.size()-1).getY() + ")");
                for(Person a:mas)
                    window.getGraphics().drawImage(a.getImage(), (int) a.getX(), (int)a.getY(), 100, 200, null);
                yurCount++;
                pos1 += 100;
            }
        }
    }
}

