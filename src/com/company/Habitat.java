package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;


public class Habitat {
    private int widht;
    private int height;
    private ArrayList<Ant> mas;
    private Timer startTime;
    private int warriorCount, workerCount;
    private long timeFromStart = 0;
    private float N1, N2, P1, P2;
    private AntFactory antFactory;
    private boolean firstTimeRun = false;

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidht(int widht) {
        this.widht = widht;
    }

    public int getHeight() {
        return height;
    }

    public int getWidht() {
        return widht;
    }


    Habitat(int widht, int height) {
        this.widht = widht;
        this.height = height;
        antFactory = new AntFactory();

    }

    Habitat() {
        this.height = 840;
        this.widht = 1224;
        antFactory = new AntFactory();
        mas = new ArrayList<Ant>();
    }

    public void init(float N1, float N2, float P1, float P2) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.P2 = P2;
        JFrame window = new JFrame("Muravei)");
        window.setSize(widht, height);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocation(400, 100);

        JPanel panel = new JPanel();
        JLabel labelTime = new JLabel();
        labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
        labelTime.setFont(new Font("TimesRoman", Font.ITALIC, 40));
        labelTime.setVerticalAlignment(JLabel.TOP);
        labelTime.setHorizontalAlignment(JLabel.RIGHT);
        labelTime.setVisible(false);
        panel.add(labelTime);

        window.add(panel);

        startTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeFromStart++;
                labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                update(timeFromStart, window);
            }
        });

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                switch (e.getKeyChar()) {
                    case 'b':
                        if (!firstTimeRun) {
                            firstTimeRun = true;
                            start(window);
                        }
                        break;
                    case 'e':
                        startTime.stop();
                        statistic(window);
                        window.repaint();
                        workerCount = 0;
                        warriorCount = 0;
                        timeFromStart = 0;
                        labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                        firstTimeRun = false;
                        mas = null;
                        break;
                    case 't':
                        if (labelTime.isVisible())
                            labelTime.setVisible(false);
                        else labelTime.setVisible(true);
                        break;
                }
            }
        });
        window.setVisible(true);
      //  window.getGraphics().drawImage(new ImageIcon("D:\\Java\\laba_1_swing\\out\\production\\laba_1_swing\\com\\company\\icon.jpg").getImage(),0,0,widht,height,null);
    }

    private void start(JFrame window) {
        System.out.println("Start");
        mas = new ArrayList<Ant>();
        startTime.start();
    }

    private void statistic(JFrame window) {
        System.out.println("Statistic");
        JDialog dialog = new JDialog(window, "Statistic");

        JLabel dialogLabel1 = new JLabel("Warriors: " + warriorCount);
        JLabel dialogLabel2 = new JLabel("Workers:" + workerCount);
        JLabel dialogLabel3 = new JLabel("Sum: " + (warriorCount + workerCount));
        JLabel dialogLabel4 = new JLabel("Work Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);

        dialogLabel1.setFont(new Font("Arial", Font.BOLD, 60));
        dialogLabel1.setForeground(Color.cyan);

        dialogLabel2.setFont(new Font("TimesRoman", Font.BOLD, 12));
        dialogLabel2.setForeground(Color.BLUE);

        dialogLabel3.setFont(new Font("Callibri", Font.BOLD, 32));
        dialogLabel3.setForeground(Color.black);

        dialogLabel4.setFont(new Font("Courier", Font.BOLD, 40));
        dialogLabel4.setForeground(Color.MAGENTA);

        dialog.setLayout(new GridLayout(4, 1));

        dialog.add(dialogLabel1);
        dialog.add(dialogLabel2);
        dialog.add(dialogLabel3);
        dialog.add(dialogLabel4);

        dialog.setSize(500, 500);

        dialog.setVisible(true);
    }

    private void settings() {
        System.out.println("setting");
    }

    private void update(float time, JFrame window) {
        if (time % N1 == 0) {
            if (Math.random() < P1) {
                mas.add(antFactory.createWarrior((float) (Math.random() * widht - 100), (float) Math.random() * height - 200));
                System.out.println("WarriorCreate(" + mas.get(mas.size()-1).getX() + "," + mas.get(mas.size()-1).getY() );
                for(Ant a:mas)
                    window.getGraphics().drawImage(a.getImage(), (int) a.getX(), (int) a.getY(), 100, 200, null);
                warriorCount++;
            }
        }
        if (time % N2 == 0) {
            if (Math.random() < P2) {
                mas.add(antFactory.createWorker((float) Math.random() * widht - 100, (float) Math.random() * height - 200));

                System.out.println("WorkerCreate("+ mas.get(mas.size()-1).getX() + "," + mas.get(mas.size()-1).getY() );
                for(Ant a:mas)
                    window.getGraphics().drawImage(a.getImage(), (int) a.getX(), (int)a.getY(), 100, 200, null);
                workerCount++;
            }
        }
    }
}

