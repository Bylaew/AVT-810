package com.Ant_lab;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class Habitat
{
    private int height;
    private int width;
    private int count_ant_work, count_ant_war;
    private ArrayList<Ant> Array;
    private Antfactory antfactory;
    private Timer time_start;
    private long starting_time = 0;
    private float Cnt1,Cnt2,p1,p2;
    private boolean First_run = false;

    public void SetWidth(int width)
    {
        this.width = width;
    }

    public void SetHeight(int height)
    {
        this.height = height;
    }

    public int GetWidth()
    {
        return width;
    }

    public int GetHeight()
    {
        return height;
    }

    Habitat(int width,int height)
    {
        this.width = width;
        this.height = height;
        antfactory = new Antfactory();
    }

    Habitat()
    {
        this.width = 1024;
        this.height = 768;
        antfactory = new Antfactory();
        Array = new ArrayList<Ant>();
    }

    void initialize(float Cnt1,float Cnt2,float p1,float p2)
    {
        this.Cnt1 = Cnt1;
        this.Cnt2 = Cnt2;
        this.p1 = p1;
        this.p2 = p2;


        JFrame Myframe = new JFrame("Lab1_Brikun_Ants");
        Myframe.setSize(1024,768);
        Myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Myframe.setLocation(400, 100);

        JPanel panel = new JPanel();

        JLabel TimeLabel = new JLabel(); //Отображение таймера
        TimeLabel.setText(starting_time / 60 + ":" + ((starting_time % 60 < 10) ? "0" : "") + starting_time % 60);
        TimeLabel.setFont(new Font("TimesRoman", Font.BOLD, 40));
        TimeLabel.setVerticalAlignment(JLabel.BOTTOM);
        TimeLabel.setHorizontalAlignment(JLabel.RIGHT);

        panel.add(TimeLabel);//Добавить метку таймера на панель
        TimeLabel.setVisible(false);
        Myframe.add(panel);

        time_start = new Timer(1000,new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            starting_time++;
            TimeLabel.setText(starting_time / 60 + ":" + ((starting_time % 60 < 10) ? "0" : "") + starting_time % 60);
            update(starting_time, Myframe);
        }
        });

        Myframe.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                super.keyTyped(e);
                switch (e.getKeyChar())
                {
                    case 'b':
                        if (!First_run)
                        {
                            First_run = true;
                            Start(Myframe);
                        }
                        break;
                    case 'e':
                        time_start.stop();
                        Info(Myframe);
                        panel.repaint();
                        count_ant_work = 0;
                        count_ant_war = 0;
                        starting_time = 0;
                        TimeLabel.setText(starting_time / 60 + ":" + ((starting_time % 60 < 10) ? "0" : "") + starting_time % 60);
                        First_run = false;
                        Array = null;
                        break;
                    case 't':
                        if (TimeLabel.isVisible())
                            TimeLabel.setVisible(false);
                        else TimeLabel.setVisible(true);
                        break;
                }
            }
        });

        Myframe.setVisible(true);
        Myframe.setLocationRelativeTo(null);
    }

    private void Start(JFrame Myframe)
    {
        System.out.println("Start");
        Array = new ArrayList<Ant>();
        time_start.start();
    }

    private void Info(JFrame Myframe)//------Статистика по программе-------
{
    System.out.println("Statistics");
    JDialog Dialog = new JDialog(Myframe, "Statistics");

    JLabel Label1 = new JLabel("Ant warriors: " + count_ant_war);
    JLabel Label2 = new JLabel("Ant workers:" + count_ant_work);
    JLabel Label3 = new JLabel("Sum: " + (count_ant_war + count_ant_work));
    JLabel Label4 = new JLabel("Work Time: " + starting_time / 60 + ":" + ((starting_time % 60 < 10) ? "0" : "") + starting_time % 60);

    //-------------------Шрифты------------------------------------------------
    Label1.setFont(new Font("TimesRoman", Font.BOLD, 12));
    Label1.setForeground(Color.black);

    Label2.setFont(new Font("Callibri", Font.BOLD, 24));
    Label2.setForeground(Color.RED);

    Label3.setFont(new Font("Tahoma", Font.BOLD, 36));
    Label3.setForeground(Color.green);

    Label4.setFont(new Font("Impact", Font.BOLD, 48));
    Label4.setForeground(Color.blue);

    Dialog.setLayout(new GridLayout(4, 1));//Менеджер компоновки GridLayout

    Dialog.add(Label1);
    Dialog.add(Label2);
    Dialog.add(Label3);
    Dialog.add(Label4);

    Dialog.setSize(500, 500);

    Dialog.setVisible(true);
}

    private void update(float time, JFrame Myframe)
    {
        if (time % Cnt1 == 0)
        {
            if (Math.random() < p1)
            {
                Array.add(antfactory.createWar((float) (Math.random() * width - 100), (float) Math.random() * height - 200));
                System.out.println("WarriorCreate(" + Array.get(Array.size()-1).getX() + "," + Array.get(Array.size()-1).getY() + ")" );
                for(Ant a:Array)
                    Myframe.getGraphics().drawImage(a.getImage(), (int) a.getX(), (int) a.getY(), 100, 200, null);
                count_ant_war++;
            }
        }
        if (time % Cnt2 == 0)
        {
            if (Math.random() < p2)
            {
                Array.add(antfactory.createWork((float) Math.random() * width - 100, (float) Math.random() * height - 200));

                System.out.println("WorkerCreate("+ Array.get(Array.size()-1).getX() + "," + Array.get(Array.size()-1).getY() + ")" );
                for(Ant a:Array)
                    Myframe.getGraphics().drawImage(a.getImage(), (int) a.getX(), (int)a.getY(), 100, 200, null);
                count_ant_work++;
            }
        }
    }
    }