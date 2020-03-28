package com.company;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import java.util.TimerTask;
import javax.swing.*;
import java.util.Timer;

public class Habitat {
    private int max=1000;
    private int NumberRabbits=0;
    private AbstractRabbit arr[]=new AbstractRabbit[max];
    private JFrame frame;
    private Timer mTimer = new Timer();
    private int NumberAlbino=0;
    private int CommonRabbit;
    private boolean ShowTime=false;
    private boolean simulate=true;
    private long currentTime;
    private JPanel panel;

    public Habitat() {
        Panel();
        frame=new JFrame("Window");
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Keys();
    }

    public void Keys()
    {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_B)
                {
                    arr=new AbstractRabbit[max];
                    mTimer=new Timer();
                    NumberRabbits=0;
                    currentTime=0;
                    CommonRabbit=0;
                    NumberAlbino=0;
                    simulate=true;
                    Start();
                }
                if(e.getKeyCode()==KeyEvent.VK_E)
                {
                    mTimer.cancel();
                    simulate=false;
                    frame.repaint();
                }
                if(e.getKeyCode()==KeyEvent.VK_T)
                {
                    if(ShowTime) {
                        ShowTime=false;
                        frame.repaint();
                    }
                    else
                    {
                        ShowTime=true;
                        frame.repaint();
                    }
                }
            }
        });
    }

    public void Panel()
    {
        panel=new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(simulate) {
                    for (int i = 0; i < NumberRabbits; i++) {

                        try {
                            g.drawImage(arr[i].getImage(), arr[i].getX(), arr[i].getY(), null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (ShowTime) {
                        g.setColor(Color.MAGENTA);
                        g.setFont(new Font("Times Roman", Font.BOLD, 15));
                        g.drawString("Simulation " + currentTime + "ms", 0, 550);
                    }
                }
                else
                {
                    g.setColor(Color.RED);
                    g.setFont(new Font("Courier", Font.ITALIC+Font.BOLD, 15));
                    g.drawString ("Simulation is running for " + currentTime + "ms",380,270 );
                    g.setColor(Color.darkGray);
                    g.drawString("ALL Rabbits "+NumberRabbits,380,250);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Helvetica", Font.PLAIN+Font.BOLD, 15));
                    g.drawString("Albino rabbit "+NumberAlbino,380,220);
                    g.setColor(Color.BLUE);
                    g.drawString("Common rabbit "+CommonRabbit,380,235);
                }
            }};
    }

    private class Updater extends TimerTask{
        private Habitat mHabitat= null;
        private boolean FirstRun=true;

        public Updater(Habitat habitat)
        {
            mHabitat=habitat;
        }
        @Override
        public void run() {
                currentTime +=100;
            mHabitat.Update(currentTime);
        }
    }

    public void Start()
    {
        mTimer.schedule(new Updater(this),0,100);
    }

    public void Update(double elapseTime){
        double N1=400;
        double N2=500;
        float k= (float) 0.1;
        double P1=0.3;
        Random r=new Random();
        ConcreteFactory obj = new ConcreteFactory();
        if(elapseTime%N2==0 && NumberAlbino<k*NumberRabbits) {
            arr[NumberRabbits++] = obj.createAlbinoRabbit();
            NumberAlbino++;
        }
        if(elapseTime%N1==0 && r.nextInt(1)<P1) {
            arr[NumberRabbits++] = obj.createRabbit();
             CommonRabbit++;
        }
        frame.repaint();
    }

}