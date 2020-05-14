package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

class Singleton {
    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null)
            instance = new Singleton();
        return instance;
    }

    private Vector<Ant> array = null;
    private HashSet<Integer> indef = null;
    private TreeMap<Integer, Integer> treeTime = null;

    public void setIndef(HashSet<Integer> indef) {
        this.indef = indef;
    }

    public void setTreeTime(TreeMap<Integer, Integer> treeTime) {
        this.treeTime = treeTime;
    }

    public void setArray(Vector<Ant> arr) {
        array = arr;
    }

    public synchronized HashSet<Integer> getIndef() {
        return indef;
    }

    public synchronized TreeMap<Integer, Integer> getTreeTime() {
        return treeTime;
    }

    public synchronized Vector<Ant> getArray() {
        return array;
    }

}

public class Habitat {
    BufferedImage backGround;
    JPanel mainPanel = new JPanel();

    private int widht;
    private int height;
    private  Singleton mas = Singleton.getInstance();
    private Timer startTime;
    private int warriorCount, workerCount;
    private long timeFromStart = 0;
    private float N1, N2, P1, P2;
    private AntFactory antFactory;
    private boolean firstTimeRun = false;
    boolean isVisibleSettings = false;
    boolean isGoing = false;

    WarrAI warrAI = new WarrAI();
    WorkAI workAI = new WorkAI();

    int V = 60;
    int R = 60;

    int lifeWarr;
    int lifeWork;

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidht(int widht) {
        this.widht = widht;
    }

    public int getHeight() {
        return height;
    }

    public long getTimeFromStart() {
        return timeFromStart;
    }

    public int getWidht() {
        return widht;
    }

    public synchronized Singleton getMas(){return mas;}


    Habitat(int widht, int height) {
        this.widht = widht;
        this.height = height;
        antFactory = new AntFactory();

    }

    Habitat() {
        try {
            backGround = ImageIO.read(new File("src/res/background1.jpg")); //починить
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.height = 840;
        this.widht = 1224;
        antFactory = new AntFactory();
    }

    public void init() {
        warrAI.setHabitat(this);

        JFrame window = new JFrame("Muravei)");
        String[] a = {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
        JComboBox p1 = new JComboBox(a);
        JComboBox p2 = new JComboBox(a);
        TextField n1 = new TextField("2");
        TextField n2 = new TextField("3");
        TextField t1 = new TextField("10");
        TextField t2 = new TextField("10");

        JPanel buttonPanel = new JPanel();

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton settingsButton = new JButton("Settings");
        JButton showButton = new JButton("Show current Ant");

        JLabel labelTime = new JLabel();

        CheckboxGroup infoGroup = new CheckboxGroup();
        Checkbox showTime = new Checkbox("Показывать время симуляции", infoGroup, true);
        Checkbox show_Time = new Checkbox("Скрыть время симуляции", infoGroup, false);
        Checkbox showInfo = new Checkbox("Показывать информацию", true);

        JToolBar toolBar = new JToolBar();
        MenuBar menu = new MenuBar();
        Menu mSim = new Menu("Simulation");
        MenuItem mStart = new MenuItem("Start");
        MenuItem mStop = new MenuItem("Stop");
        Menu mSettings = new Menu("Settings");
        Menu mHelp = new Menu("Help");

        CheckboxMenuItem statisticCheckBox = new CheckboxMenuItem("Show statistic", true);
        CheckboxMenuItem timeCheckBox = new CheckboxMenuItem("Show time", true);

        JButton toolStart = new JButton("1");

        JButton toolStop = new JButton("2");

        JButton toolSettings = new JButton("3");

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyChar());
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        if (!firstTimeRun) {
                            System.out.println(checkStr(n1.getText()));
                            if (checkStr(n1.getText()) && checkStr(n2.getText()) && checkStr(t1.getText()) && checkStr(t2.getText())) {
                                stopButton.setEnabled(true);
                                startButton.setEnabled(false);
                                showButton.setEnabled(true);
                                N1 = Float.parseFloat(n1.getText());
                                N2 = Float.parseFloat(n2.getText());
                                P1 = ((float) (p1.getSelectedIndex() + 1)) / 10;
                                P2 = ((float) (p2.getSelectedIndex() + 1)) / 10;
                                lifeWarr = Integer.parseInt(t1.getText());
                                lifeWork = Integer.parseInt(t2.getText());
                                firstTimeRun = true;
                                start(window);
                            } else {
                                JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);
                            }
                        }
                        break;
                    case KeyEvent.VK_E:
                        if (firstTimeRun) {
                            startTime.stop();
                            int result = 0;
                            if (showInfo.getState())
                                result = statistic(window);
                            System.out.println(result);
                            if (result == 0) {
                                window.repaint();
                                stopButton.setEnabled(false);
                                startButton.setEnabled(true);
                                toolStart.setEnabled(true);
                                toolStop.setEnabled(false);
                                showButton.setEnabled(false);
                                workerCount = 0;
                                warriorCount = 0;
                                timeFromStart = 0;
                                firstTimeRun = false;
                                labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                                mas.setArray(null);
                            } else {
                                startTime.start();

                            }
                        }
                        break;
                    case KeyEvent.VK_T:
                        if (labelTime.isVisible())
                            labelTime.setVisible(false);
                        else labelTime.setVisible(true);
                        break;
                }
            }
        });
        window.setFocusable(true);

        window.setSize(widht, height);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocation(400, 100);
        window.setLayout(null);

        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, height - 210, widht, 210);
        buttonPanel.setBackground(Color.CYAN);

        labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
        labelTime.setFont(new Font("TimesRoman", Font.ITALIC, 40));
        labelTime.setBounds(1100, 50, 100, 40);
        labelTime.setForeground(Color.black);
        labelTime.setVisible(true);

        showTime.setBounds(825, 20, 200, 20);
        show_Time.setBounds(825, 50, 200, 20);
        showInfo.setBounds(1025, 20, 200, 20);

        showTime.setVisible(true);
        show_Time.setVisible(true);
        showInfo.setVisible(true);

        buttonPanel.add(showInfo);
        buttonPanel.add(showTime);
        buttonPanel.add(show_Time);

        JLabel n1Label = new JLabel("n1");
        JLabel n2Label = new JLabel("n2");
        JLabel p1Label = new JLabel("p1");
        JLabel p2Label = new JLabel("p2");
        JLabel t1Label = new JLabel("t1");
        JLabel t2Label = new JLabel("t2");

        n1Label.setBounds(240, 100, 30, 30);
        n2Label.setBounds(580, 100, 30, 30);
        p1Label.setBounds(70, 100, 30, 30);
        p2Label.setBounds(410, 100, 30, 30);
        t1Label.setBounds(750, 100, 30, 30);
        t2Label.setBounds(920, 100, 30, 30);

        buttonPanel.add(n1Label);
        buttonPanel.add(n2Label);
        buttonPanel.add(p1Label);
        buttonPanel.add(p2Label);
        buttonPanel.add(t1Label);
        buttonPanel.add(t2Label);

        p1.setBounds(100, 100, 120, 30);
        n1.setBounds(270, 100, 120, 30);
        p2.setBounds(440, 100, 120, 30);
        n2.setBounds(610, 100, 120, 30);
        t1.setBounds(780, 100, 120, 30);
        t2.setBounds(950, 100, 120, 30);

        n1.setVisible(false);
        n2.setVisible(false);
        p1.setVisible(false);
        p2.setVisible(false);
        t1.setVisible(false);
        t2.setVisible(false);

        n1Label.setVisible(false);
        n2Label.setVisible(false);
        p1Label.setVisible(false);
        p2Label.setVisible(false);
        t1Label.setVisible(false);
        t2Label.setVisible(false);
        buttonPanel.add(p1);
        buttonPanel.add(p2);
        buttonPanel.add(n1);
        buttonPanel.add(n2);
        buttonPanel.add(t1);
        buttonPanel.add(t2);

        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBounds(0, 40, widht, height - 250);
        mainPanel.setLayout(null);
        buttonPanel.add(labelTime);

        window.add(mainPanel).requestFocus();
        window.add(buttonPanel).requestFocus();

        mStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!firstTimeRun) {
                    System.out.println(checkStr(n1.getText()));
                    if (checkStr(n1.getText()) && checkStr(n2.getText()) && checkStr(t1.getText()) && checkStr(t2.getText())) {
                        stopButton.setEnabled(true);
                        startButton.setEnabled(false);
                        showButton.setEnabled(true);
                        N1 = Float.parseFloat(n1.getText());
                        N2 = Float.parseFloat(n2.getText());
                        lifeWarr = Integer.parseInt(t1.getText());
                        lifeWork = Integer.parseInt(t2.getText());
                        P1 = ((float) (p1.getSelectedIndex() + 1)) / 10;
                        P2 = ((float) (p2.getSelectedIndex() + 1)) / 10;

                        firstTimeRun = true;
                        start(window);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);

                    }
                }
            }

        });
        mStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstTimeRun) {
                    startTime.stop();
                    int result = 0;
                    if (showInfo.getState())
                        result = statistic(window);
                    System.out.println(result);
                    if (result == 0) {
                        window.repaint();
                        stopButton.setEnabled(false);
                        startButton.setEnabled(true);
                        toolStart.setEnabled(true);
                        toolStop.setEnabled(false);
                        showButton.setEnabled(false);
                        workerCount = 0;
                        warriorCount = 0;
                        timeFromStart = 0;
                        firstTimeRun = false;
                        labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                        mas.setArray(null);
                    } else {
                        startTime.start();

                    }
                }
            }
        });
        MenuItem mHelp1 = new MenuItem("Help");
        mHelp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog helpDialog = new JDialog(window, "Help");
                helpDialog.setBounds(100, 200, 600, 400);
                JTextArea contentArea = new JTextArea();
                contentArea.setFont(new Font("Arial", Font.ITALIC, 24));
                contentArea.setText("p1 - вероятность появления муровья война\n" +
                        "p2 - вероятность появления работника\n" +
                        "n1 - время вероятного появления война\n" +
                        "p2 - вреся вероятного появления работника\n" +
                        "t - скрыть/показать время\n" +
                        "b - начать симуляцию\n" +
                        "e - закончить симуляцию\n" +
                        "t1 - время жизни война\n" +
                        "t2 - время жизни работника");
                contentArea.setLineWrap(true);
                contentArea.setWrapStyleWord(true);
                contentArea.setLocation(100, 200);
                contentArea.setEditable(false);

                helpDialog.add(contentArea);
                helpDialog.setVisible(true);
            }
        });
        mHelp.add(mHelp1);

        menu.add(mSim);
        menu.add(mSettings);
        menu.setHelpMenu(mHelp);

        timeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (timeCheckBox.getState()) {
                    show_Time.setState(false);
                    showTime.setState(true);
                    labelTime.setVisible(true);
                } else {
                    show_Time.setState(true);
                    showTime.setState(false);
                    labelTime.setVisible(false);
                }
            }
        });
        mSim.add(mStart);
        mSim.add(mStop);
        mSettings.add(timeCheckBox);
        mSettings.add(statisticCheckBox);
        window.setMenuBar(menu);

        show_Time.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                timeCheckBox.setState(false);
                labelTime.setVisible(false);
            }
        });
        showTime.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                timeCheckBox.setState(true);
                labelTime.setVisible(true);
            }
        });

        startTime = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeFromStart++;
                labelTime.setText(timeFromStart / 600 + ":" + ((timeFromStart/10 % 60 < 10) ? "0" : "") + timeFromStart / 10 % 60);
                update(timeFromStart, mainPanel);
            }
        });

        startButton.setBounds(50, 20, 150, 50);
        stopButton.setEnabled(false);
        buttonPanel.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(checkStr(n1.getText()));
                if (checkStr(n1.getText()) && checkStr(n2.getText()) && checkStr(t1.getText()) && checkStr(t2.getText())) {
                    stopButton.setEnabled(true);
                    startButton.setEnabled(false);
                    toolStart.setEnabled(false);
                    toolStop.setEnabled(true);
                    showButton.setEnabled(true);
                    N1 = Float.parseFloat(n1.getText());
                    N2 = Float.parseFloat(n2.getText());
                    P1 = ((float) (p1.getSelectedIndex() + 1)) / 10;
                    P2 = ((float) (p2.getSelectedIndex() + 1)) / 10;
                    lifeWarr = Integer.parseInt(t1.getText());
                    lifeWork = Integer.parseInt(t2.getText());

                    firstTimeRun = true;
                    start(window);
                } else {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);

                }
            }
        });

        stopButton.setBounds(250, 20, 150, 50);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTime.stop();
                int result = 0;
                if (showInfo.getState())
                    result = statistic(window);
                System.out.println(result);
                if (result == 0) {
                    window.repaint();
                    stopButton.setEnabled(false);
                    startButton.setEnabled(true);
                    toolStart.setEnabled(true);
                    toolStop.setEnabled(false);
                    showButton.setEnabled(false);
                    workerCount = 0;
                    warriorCount = 0;
                    timeFromStart = 0;
                    labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                    firstTimeRun = false;
                    mas.setArray(null);
                    warrAI.setGoing(false);
                    workAI.setGoing(false);


                } else {
                    startTime.start();

                }
            }
        });
        buttonPanel.add(stopButton);

        settingsButton.setBounds(450, 20, 150, 50);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isVisibleSettings) {
                    n1.setVisible(false);
                    n2.setVisible(false);
                    p1.setVisible(false);
                    p2.setVisible(false);
                    t1.setVisible(false);
                    t2.setVisible(false);
                    n1Label.setVisible(false);
                    n2Label.setVisible(false);
                    p1Label.setVisible(false);
                    p2Label.setVisible(false);
                    t1Label.setVisible(false);
                    t2Label.setVisible(false);
                    isVisibleSettings = false;
                    n1.setFocusable(false);
                    n2.setFocusable(false);
                    p1.setFocusable(false);
                    p2.setFocusable(false);
                    t1.setFocusable(false);
                    t2.setFocusable(false);
                    window.requestFocus();
                } else {
                    n1.setVisible(true);
                    n2.setVisible(true);
                    p1.setVisible(true);
                    p2.setVisible(true);
                    t1.setVisible(true);
                    t2.setVisible(true);
                    n1Label.setVisible(true);
                    n2Label.setVisible(true);
                    p1Label.setVisible(true);
                    p2Label.setVisible(true);
                    t1Label.setVisible(true);
                    t2Label.setVisible(true);
                    isVisibleSettings = true;

                }
            }
        });
        buttonPanel.add(settingsButton);

        showButton.setBounds(650, 20, 150, 50);
        showButton.setEnabled(false);
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTime.stop();

                String currentAnt = new String();
                for (int i = 0; i < mas.getArray().size(); i++) {
                    if (mas.getArray().get(i) instanceof Warrior) {
                        currentAnt += "Warrior id: " + mas.getArray().get(i).getIndef() + ", bornTime: "
                                + (mas.getArray().get(i).getBornTime() / 60 + ":" + ((mas.getArray().get(i).getBornTime() % 60 < 10) ? "0" : "") + mas.getArray().get(i).getBornTime() % 60) +
                                ", deathTime:" + ((mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime()) / 60) + ":" + (((mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime()) % 60 < 10) ? "0" : "")
                                + (mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime()) % 60 + "\n";
                    } else {
                        currentAnt += "Worker id: " + mas.getArray().get(i).getIndef() + ", bornTime: "
                                + (mas.getArray().get(i).getBornTime() / 60 + ":" + ((mas.getArray().get(i).getBornTime() % 60 < 10) ? "0" : "") + mas.getArray().get(i).getBornTime() % 60) +
                                ", deathTime:" + ((mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime()) / 60) + ":" + (((mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime()) % 60 < 10) ? "0" : "")
                                + (mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime()) % 60 + "\n";
                    }

                }

                JOptionPane.showMessageDialog(window, currentAnt, "Current Ant", JOptionPane.INFORMATION_MESSAGE);
                startTime.start();
            }
        });

        buttonPanel.add(showButton);


        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBounds(0, 0, widht, 40);
        toolStart.setBounds(10, 80, 50, 50);
        toolStop.setBounds(10, 150, 50, 50);
        toolStop.setEnabled(false);
        toolSettings.setBounds(10, 230, 50, 50);

        toolStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!firstTimeRun) {
                    System.out.println(checkStr(n1.getText()));
                    if (checkStr(n1.getText()) && checkStr(n2.getText()) && checkStr(t1.getText()) && checkStr(t2.getText())) {
                        stopButton.setEnabled(true);
                        startButton.setEnabled(false);
                        toolStart.setEnabled(false);
                        toolStop.setEnabled(true);
                        showButton.setEnabled(true);
                        N1 = Float.parseFloat(n1.getText());
                        N2 = Float.parseFloat(n2.getText());
                        lifeWarr = Integer.parseInt(t1.getText());
                        lifeWork = Integer.parseInt(t2.getText());
                        P1 = ((float) (p1.getSelectedIndex() + 1)) / 10;
                        P2 = ((float) (p2.getSelectedIndex() + 1)) / 10;

                        firstTimeRun = true;
                        start(window);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);

                    }
                }
            }
        });
        toolStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTime.stop();
                int result = 0;
                if (showInfo.getState())
                    result = statistic(window);
                System.out.println(result);
                if (result == 0) {
                    window.repaint();
                    stopButton.setEnabled(false);
                    startButton.setEnabled(true);
                    toolStart.setEnabled(true);
                    toolStop.setEnabled(false);
                    showButton.setEnabled(false);
                    workerCount = 0;
                    warriorCount = 0;
                    timeFromStart = 0;
                    firstTimeRun = false;
                    labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                    mas.setArray(null);
                } else {
                    startTime.start();

                }
            }
        });
        toolSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isVisibleSettings) {
                    n1.setVisible(false);
                    n2.setVisible(false);
                    p1.setVisible(false);
                    p2.setVisible(false);
                    t1.setVisible(false);
                    t2.setVisible(false);
                    n1Label.setVisible(false);
                    t1Label.setVisible(false);
                    t2Label.setVisible(false);
                    n2Label.setVisible(false);
                    p1Label.setVisible(false);
                    p2Label.setVisible(false);
                    isVisibleSettings = false;
                } else {
                    n1.setVisible(true);
                    t1.setVisible(true);
                    t2.setVisible(true);
                    n2.setVisible(true);
                    p1.setVisible(true);
                    p2.setVisible(true);
                    n1Label.setVisible(true);
                    t2Label.setVisible(true);
                    n2Label.setVisible(true);
                    p1Label.setVisible(true);
                    p2Label.setVisible(true);
                    isVisibleSettings = true;
                }
            }
        });

        toolBar.add(toolStart);
        toolBar.add(toolStop);
        toolBar.add(toolSettings);
        toolBar.setBackground(Color.YELLOW);
        window.add(toolBar).requestFocus();

        System.out.println(window.isFocusable());

        window.setVisible(true);
        System.out.println(window.isFocusable());

        window.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (n1.isFocusable() || n2.isFocusable() || p1.isFocusable() || p2.isFocusable() || show_Time.isFocusable() || showInfo.isFocusable() || showTime.isFocusable())
                    ;
                else
                    window.requestFocus();
            }
        });
    }

    private void start(JFrame window) {
        System.out.println("Start");
        isGoing = true;
        mas.setArray(new Vector<>());
        mas.setIndef(new HashSet<>());
        mas.setTreeTime(new TreeMap<>());
        startTime.start();
        warrAI.start();
        workAI.start();
        warrAI.setGoing(true);
        workAI.setGoing(true);

    }

    private int statistic(JFrame window) {
        return JOptionPane.showConfirmDialog(window, new String[]{"Warriors: " + warriorCount, "Workers:" + workerCount, "Sum: " + (warriorCount + workerCount), "Work Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60}, "Statistic", JOptionPane.OK_CANCEL_OPTION);
    }

    private  void update(float time, JComponent g) {

        for (int i = 0; i < mas.getArray().size(); i++) {
            if (mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime() <= (int) time/10) {

                mas.getIndef().remove(mas.getArray().get(i).getIndef());
                mas.getTreeTime().remove(mas.getArray().get(i).getIndef());
                mas.getArray().remove(i);
                System.out.println("NUUU");

            }
        }
        if (time % (N1*10) == 0) {
            if (Math.random() < P1) {
                mas.getArray().add(antFactory.createWarrior((float) (Math.random() * (widht - 100 + 1)), (float) Math.random() * (height - 490) + 40, lifeWarr, (int) time/10, warriorCount + workerCount));
                mas.getIndef().add(mas.getArray().get(mas.getArray().size() - 1).getIndef());
                mas.getTreeTime().put(mas.getArray().get(mas.getArray().size() - 1).getIndef(), mas.getArray().get(mas.getArray().size() - 1).getBornTime());
                System.out.println("WarriorCreate(" + mas.getArray().get(mas.getArray().size() - 1).getX() + "," + mas.getArray().get(mas.getArray().size() - 1).getY() + ")");
                warriorCount++;
            }
        }
        if (time % (N2*10) == 0) {
            if (Math.random() < P2) {
                mas.getArray().add(antFactory.createWorker((float) (Math.random() * (widht - 100 + 1)), (float) Math.random() * (height - 490) + 40, lifeWork, (int) time/10, warriorCount + workerCount));
                mas.getIndef().add(mas.getArray().get(mas.getArray().size() - 1).getIndef());
                mas.getTreeTime().put(mas.getArray().get(mas.getArray().size() - 1).getIndef(), mas.getArray().get(mas.getArray().size() - 1).getBornTime());
                System.out.println("WorkerCreate(" + mas.getArray().get(mas.getArray().size() - 1).getX() + "," + mas.getArray().get(mas.getArray().size() - 1).getY() + ")");
                workerCount++;
            }
        }

        draw(g);

    }

    public synchronized void draw(JComponent g){

        int w = g.getWidth(), h = g.getHeight();
        BufferedImage fieldImage;
        fieldImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics fieldImageGraphics = fieldImage.getGraphics();
        fieldImageGraphics.drawImage(backGround, 0, 0, null);
        for (int i = 0; i < mas.getArray().size(); i++)
            fieldImageGraphics.drawImage(mas.getArray().get(i).getImage(), (int) mas.getArray().get(i).getX(), (int) mas.getArray().get(i).getY(), 100, 200, null);

        g.getGraphics().drawImage(fieldImage, 0, 0, w, h, null);

    }

    private boolean checkStr(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < str.length(); i++)
            if (a[i] >= '0' && a[i] <= '9')
                continue;
            else if (a[i] == '.')
                continue;
            else return false;

        return true;
    }

}

