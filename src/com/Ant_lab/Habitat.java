package com.Ant_lab;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import static java.awt.event.KeyEvent.*;

class Singleton{
    private static Singleton instance;
    private Singleton(){};

    public static Singleton getInstance()
    {
        if(instance == null)
            instance = new Singleton();
        return instance;
    }

    private Ant[] array = null;

    public void setArray(Ant[] arr)
    {
        array = arr;
    }

    public Ant[] getArray()
    {
        return array;
    }

    public void add(Ant ant, int count)
    {
        array[count] = ant;
    }

    public Ant getAnt(int i)
    {
        return array[i];
    }
}

public class Habitat
{
    private int height;
    private int width;
    private int count_ant_work, count_ant_war;

    Singleton array = Singleton.getInstance();

    private Antfactory antfactory;
    private Timer time_start;
    private long starting_time = 0;
    private float Cnt1,Cnt2,p1,p2,T1,T2;
    public boolean busy = false;
    public boolean show_chosen = false;

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
    }

    void initialize(float T1,float T2,float p1,float p2)
    {
        /*this.Cnt1 = Cnt1;
        this.Cnt2 = Cnt2;*/
        this.p1 = p1;
        this.p2 = p2;
        this.T1 = T1;
        this.T2 = T2;

        Cnt1 = T1;
        Cnt2 = T2;

        //-------Главное окно-------
        JFrame Myframe = new JFrame("Lab2_Ants");//Основное окно JFrame
        Myframe.setSize(1024,768);
        Myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Myframe.setLocation(400, 100);
        Border border = BorderFactory.createLineBorder(Color.black);//Граница

        //-----ПАНЕЛЬ СИМУЛЯЦИИ-----
        JPanel simulation_panel = new JPanel();
        simulation_panel.setBorder(border);

        //---------МЕНЮ---------
        JMenuBar Menubar = new JMenuBar();
        JMenu Menu = new JMenu("Menu");
        JMenu Menutime = new JMenu("Time");

        JMenuItem Menustart = new JMenuItem("Start");
        JMenuItem Menustop = new JMenuItem("Stop");

        JRadioButtonMenuItem showTime = new JRadioButtonMenuItem("Show time");
        JRadioButtonMenuItem hideTime = new JRadioButtonMenuItem("Hide time");
        hideTime.setSelected(true);

        Menubar.add(Menu);
        Menu.add(Menustart);
        Menu.add(Menustop);

        ButtonGroup Group = new ButtonGroup();
        Group.add(showTime);
        Group.add(hideTime);

        Menu.add(Menutime);
        Menutime.add(showTime);
        Menutime.add(hideTime);

        JCheckBoxMenuItem showDialogMenuItem = new JCheckBoxMenuItem("Show dialog");
        Menu.add(showDialogMenuItem);

        Myframe.setJMenuBar(Menubar);

        //---------Таймер---------
        JLabel TimeLabel = new JLabel(); //Метка таймера
        TimeLabel.setBorder(border);
        TimeLabel.setText(starting_time / 60 + ":" + ((starting_time % 60 < 10) ? "0" : "") + starting_time % 60);
        TimeLabel.setFont(new Font("TimesRoman", Font.BOLD, 40));
        TimeLabel.setVerticalAlignment(JLabel.CENTER);//Расположение таймера
        TimeLabel.setHorizontalAlignment(JLabel.LEFT);
        TimeLabel.setVisible(false);//Видимость
        simulation_panel.add(TimeLabel);//Добавить метку таймера на панель

        //----------------------------ПАНЕЛЬ НАСТРОЙКИ-----------------------------
        JPanel settings_panel = new JPanel();//Общая панель настроек
        settings_panel.setBorder(border);//Граница

        JPanel StartStopPanel = new JPanel();//Панель для кнопок старт стоп
        ButtonGroup start_stop = new ButtonGroup();//Группа кнопок стар стоп

        JToggleButton Start_simulate = new JToggleButton("Start");
        JToggleButton Stop_simulate = new JToggleButton("Stop");

        JCheckBox showDialogBox = new JCheckBox("Show dialog");
        settings_panel.add(showDialogBox);//Диалоговое окно

        JRadioButton showTimeButton= new JRadioButton("Show time",false);
        JRadioButton hideTimeButton= new JRadioButton("Hide time",true);

        JPanel Show_Hide_Panel = new JPanel();//Панель для Show Hide time
        Show_Hide_Panel.add(showTimeButton);//галочки показать,скрыть время
        Show_Hide_Panel.add(hideTimeButton);

        ButtonGroup s_h = new ButtonGroup();
        s_h.add(showTimeButton);// галочки группа
        s_h.add(hideTimeButton);

        //----------------------Комбобокс зоны--------------------
        JComboBox combo1 = new JComboBox();//для частоты появления
        JComboBox combo2 = new JComboBox();
        //----------------------ТЕКСТОВЫЕ ЗОНЫ--------------------
        JTextField period1 = new JTextField();//подписи
        JTextField period2 = new JTextField();

        period1.setText("1");
        period2.setText("1");

        //---------------------------------------------------------
        settings_panel.add(Start_simulate);//кнопки старт стоп
        settings_panel.add(Stop_simulate);

        start_stop.add(Start_simulate);//добавление в группу кнопок старт стоп
        start_stop.add(Stop_simulate);

        combo1.setEditable(false);
        combo2.setEditable(false);

        for (int i = 1; i <= 10;i = i+1 )//вероятность от 0.1 до 1(10% - 100%)
        {
            combo1.addItem((float)i/10);
            combo2.addItem((float)i/10);
        }
        combo1.setSelectedIndex(9);
        combo2.setSelectedIndex(9);

        settings_panel.setLayout(new GridLayout(20,1,10,5));

        settings_panel.add(StartStopPanel);
        settings_panel.add(showDialogBox);

        showDialogBox.setSelected(false);
        show_chosen = false;

        settings_panel.add(new JLabel("Probability 1"));//вероятность
        settings_panel.add(combo1);
        settings_panel.add(new JLabel("Probability 2"));
        settings_panel.add(combo2);
        settings_panel.add(Show_Hide_Panel);
        settings_panel.add(new JLabel("Period 1"));
        settings_panel.add(period1);
        settings_panel.add(new JLabel("Period 2"));
        settings_panel.add(period2);

        Myframe.add(settings_panel,BorderLayout.EAST);
        Myframe.add(simulation_panel,BorderLayout.CENTER);



        Myframe.setFocusable(true);//Фокус на главном окне
        //-----------------------------ТАЙМЕР-----------------------------
        time_start = new Timer(1000,new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
            starting_time++;
            TimeLabel.setText(starting_time / 60 + ":" + ((starting_time % 60 < 10) ? "0" : "") + starting_time % 60);
            update(starting_time, simulation_panel);
        }
        });

        //-----------------------------Функции кнопок-----------------------------
        Start_simulate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!busy)
                {
                    Start_simulate.setSelected(true);
                    Stop_simulate.setSelected(false);
                    Start(Myframe);
                    Myframe.requestFocus();
                }
            }
        });

        Stop_simulate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Start_simulate.setSelected(false);
                Stop_simulate.setSelected(true);

                if(show_chosen)
                {
                    time_start.stop();
                    Info(Myframe, Start_simulate, Stop_simulate);
                    simulation_panel.repaint();
                    Myframe.requestFocus();
                    return;
                }
                time_start.stop();
                count_ant_work = 0;
                count_ant_war = 0;
                starting_time = 0;
                array.setArray(null);
                busy = false;
                simulation_panel.repaint();
                Myframe.requestFocus();
            }
        });

        showTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TimeLabel.setVisible(true);
                showTime.setSelected(true);
                Myframe.requestFocus();
            }
        });

        hideTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TimeLabel.setVisible(false);
                hideTime.setSelected(true);
                Myframe.requestFocus();
            }
        });

        Menustart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!busy)
                {
                    Start_simulate.setSelected(true);
                    Stop_simulate.setSelected(false);
                    Start(Myframe);
                    Myframe.requestFocus();
                }
            }
        });

        Menustop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(show_chosen)
                {
                    time_start.stop();
                    Info(Myframe, Start_simulate, Stop_simulate);
                    simulation_panel.repaint();
                    Myframe.requestFocus();
                    return;
                }
                Start_simulate.setSelected(false);
                Stop_simulate.setSelected(true);
                time_start.stop();
                count_ant_work = 0;
                count_ant_war = 0;
                starting_time = 0;
                array.setArray(null);
                busy = false;
                simulation_panel.repaint();
                Myframe.requestFocus();
            }
        });

        showTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TimeLabel.setVisible(true);
                showTimeButton.setSelected(true);

                Myframe.requestFocus();
            }
        });

        hideTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TimeLabel.setVisible(false);
                hideTimeButton.setSelected(true);
                Myframe.requestFocus();
            }
        });

        showDialogBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {
                    show_chosen = true;
                    showDialogMenuItem.setSelected(true);
                    System.out.println(show_chosen);
                    System.out.println(e.getStateChange());
                }
                if (e.getStateChange()== ItemEvent.DESELECTED)
                {
                    show_chosen = false;
                    showDialogMenuItem.setSelected(false);
                    System.out.println(show_chosen);
                    System.out.println(e.getStateChange());
                }
                Myframe.requestFocus();
            }
        });

        showDialogMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {
                    show_chosen = true;
                    showDialogBox.setSelected(true);
                    System.out.println(show_chosen);
                    System.out.println(e.getStateChange());
                }
                if (e.getStateChange()== ItemEvent.DESELECTED)
                {
                    show_chosen = false;
                    showDialogBox.setSelected(false);
                    System.out.println(show_chosen);
                    System.out.println(e.getStateChange());
                }
                Myframe.requestFocus();
            }
        });

            period1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    float period;
                    try
                    {
                        period = Float.parseFloat(period1.getText());
                        if (period > 0)
                        {Cnt1 = period;
                            System.out.println(period);
                        }
                        else throw new NumberFormatException();
                    }
                    catch (NumberFormatException ex)
                    {
                        Cnt1=1;
                        eror_dialog(Myframe);
                        System.out.println("Period1 - wrong value;");
                    }
                }

        }   );

        period2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float period3;
                try
                {
                    period3 = Float.parseFloat(period2.getText());
                    if (period3 > 0)
                    {Cnt2 = period3;
                        System.out.println(period2);
                    }
                    else throw new NumberFormatException();
                }
                catch (NumberFormatException ex)
                {
                    Cnt2=1;
                    eror_dialog(Myframe);
                    System.out.println("Period2 - wrong value;");
                }
            }
        });


        //Выпадающий список
        combo1.addItemListener(e -> this.p1 = (float)combo1.getSelectedItem());
        combo2.addItemListener(e -> this.p2 = (float)combo1.getSelectedItem());

        Myframe.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
               super.keyPressed(e);//Клавиша была нажата

                switch (e.getKeyCode())
                {
                    case VK_B:
                        if(!busy)
                        {
                            Start_simulate.setSelected(true);
                            Stop_simulate.setSelected(false);
                            Start(Myframe);
                        }
                        break;

                    case VK_E:
                            Start_simulate.setSelected(false);
                            Stop_simulate.setSelected(true);

                        if(show_chosen)
                        {
                            time_start.stop();
                            Info(Myframe, Start_simulate, Stop_simulate);
                            simulation_panel.repaint();
                        }
                            time_start.stop();
                            count_ant_work = 0;
                            count_ant_war = 0;
                            starting_time = 0;
                            array.setArray(null);
                            busy = false;
                            simulation_panel.repaint(); break;

                    case VK_T:
                        if (TimeLabel.isVisible())
                        {
                            showTimeButton.setSelected(false);
                            hideTimeButton.setSelected(true);
                            TimeLabel.setVisible(false);
                        }
                        else { showTimeButton.setSelected(true);
                            hideTimeButton.setSelected(false);
                            TimeLabel.setVisible(true); }
                        break;
                }
            }
        });
        Myframe.setVisible(true);
        Myframe.setLocationRelativeTo(null);
    }

    private void Start(JFrame Myframe)//начало + массив муравьев
    {
        System.out.println("Starting simulate");
        if (!busy)
        {
        array.setArray(new Ant[500]);
        busy = true;
        }
        time_start.start();
    }

    private void Info(JFrame Myframe, JToggleButton Start_simulate, JToggleButton Stop_simulate)//------Статистика по программе(Диалоговое окно)-------
    {
    System.out.println("Statistics");
    JDialog Dialog = new JDialog(Myframe, "Statistics");

    JLabel Label1 = new JLabel("Ant warriors: " + count_ant_war);
    JLabel Label2 = new JLabel("Ant workers:" + count_ant_work);
    JLabel Label3 = new JLabel("Sum: " + (count_ant_war + count_ant_work));
    JLabel Label4 = new JLabel("Work Time: " + starting_time / 60 + ":" + ((starting_time % 60 < 10) ? "0" : "") + starting_time % 60);

    JButton Okay = new JButton("OK");
    JButton Cancel = new JButton("Cancel");

    JPanel Okay_Cancel = new JPanel();
    Okay_Cancel.setLayout(new FlowLayout());
    Okay_Cancel.add(Okay);
    Okay_Cancel.add(Cancel);


    Okay.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Start_simulate.setSelected(false);
            Stop_simulate.setSelected(true);

            count_ant_work = 0;
            count_ant_war = 0;
            starting_time = 0;
            array.setArray(null);
            busy = false;
            Dialog.dispose();
            Myframe.requestFocus();
        }
    });

    Cancel.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Start_simulate.setSelected(true);
            Stop_simulate.setSelected(false);

            Start(Myframe);
            Dialog.dispose();
            Myframe.requestFocus();
        }
    });

    //-------------------Шрифты-------------------
    Label1.setFont(new Font("TimesRoman", Font.BOLD, 16));
    Label1.setForeground(Color.black);

    Label2.setFont(new Font("TimesRoman", Font.BOLD, 16));
    Label2.setForeground(Color.black);

    Label3.setFont(new Font("TimesRoman", Font.BOLD, 16));
    Label3.setForeground(Color.black);

    Label4.setFont(new Font("TimesRoman", Font.BOLD, 16));
    Label4.setForeground(Color.black);

    Dialog.setLayout(new GridLayout(5, 1));//Менеджер компоновки GridLayout

    Dialog.add(Label1);
    Dialog.add(Label2);
    Dialog.add(Label3);
    Dialog.add(Label4);
    Dialog.add(Okay_Cancel);

    Dialog.setSize(400, 300);

    Dialog.setVisible(true);
    Dialog.setBounds(550,350,400,300);
    }

    private void eror_dialog(JFrame Myframe)// ошбика при вводе в текстовое поле невалидное значение
    {
        JDialog eror_text = new JDialog(Myframe,"Wrong value");
        JLabel wrong_message = new JLabel("You entered wrong value!");
        JLabel default_mes = new JLabel("T1 = 1 sec; T2 = 1 sec");
        JButton ok = new JButton("Okay");

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eror_text.dispose();
                Myframe.requestFocus();
            }
        });

        eror_text.setLayout(new GridLayout (3,1));
        eror_text.add(wrong_message);
        eror_text.add(default_mes);

        JPanel flow_eror = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flow_eror.add(ok);

        JPanel borderr = new JPanel(new BorderLayout());
        borderr.add(flow_eror, BorderLayout.SOUTH);

        eror_text.add(borderr);
        eror_text.setSize(250, 150);
        eror_text.setVisible(true);
        eror_text.setBounds(650,400,250,150);
    }

    private void update(float time, JPanel panel)//фабрика муравьев
    {
        if (time % Cnt1 == 0)
        {
            if (Math.random() < p1)
            {//width [10;747), height [50;657)
                array.add(antfactory.createWar(10 + (float)(Math.random()* (panel.getWidth() - 81)), 20 + (float)(Math.random()* (panel.getHeight() - 91))),count_ant_war + count_ant_work);

                System.out.println("WarriorCreate( x : " + array.getAnt(count_ant_war + count_ant_work).getX() + ", y : " + array.getAnt(count_ant_war + count_ant_work).getY() + ")\n" );
                count_ant_war++;
            }
        }
        if (time % Cnt2 == 0)
        {
            if (Math.random() < p2)
            {
                array.add(antfactory.createWork(10 + (float)(Math.random()* (panel.getWidth() - 20)),20 + (float)(Math.random()* (panel.getHeight() - 91))),count_ant_war + count_ant_work);

                System.out.println("WorkerCreate( x : "+ array.getAnt(count_ant_war + count_ant_work).getX() + ", y : " + array.getAnt(count_ant_war + count_ant_work).getY() + ")\n" );
                count_ant_work++;
            }
        }
        for (int i = 0; i < count_ant_work + count_ant_war; i++)
            panel.getGraphics().drawImage(array.getAnt(i).getImage(), (int) array.getAnt(i).getX(), (int) array.getAnt(i).getY(), 100, 200, null);
    }
}