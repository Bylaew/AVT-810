package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

class Singleton implements Serializable {
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
    private Singleton mas = Singleton.getInstance();
    private Timer startTime;
    private int warriorCount, workerCount;
    private long timeFromStart = 0;
    private float N1, N2, P1, P2;
    private AntFactory antFactory;
    private boolean firstTimeRun = false;
    private boolean isConsoleRunning = false;
    boolean isVisibleSettings = false;

    WarrAI warrAI;
    WorkAI workAI;

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

    public synchronized Singleton getMas() {
        return mas;
    }


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
        JFrame window = new JFrame("Muravei)");
        String[] a = {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
        JComboBox p1 = new JComboBox(a);
        JComboBox p2 = new JComboBox(a);


        JComboBox warrPriority = new JComboBox(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        JComboBox workPriority = new JComboBox(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});

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
        JLabel warrPriopityLabel = new JLabel("Warrior AI Priority");
        JLabel workPriopityLabel = new JLabel("Worker AI Priority");

        CheckboxGroup infoGroup = new CheckboxGroup();
        Checkbox showTime = new Checkbox("Показывать время симуляции", infoGroup, true);
        Checkbox show_Time = new Checkbox("Скрыть время симуляции", infoGroup, false);
        Checkbox showInfo = new Checkbox("Показывать информацию", true);

        JToolBar toolBar = new JToolBar();
        MenuBar menu = new MenuBar();
        Menu mSim = new Menu("Simulation");
        MenuItem mStart = new MenuItem("Start");
        MenuItem mConsole = new MenuItem("Console");
        MenuItem mSave = new MenuItem("Save as...");
        MenuItem mLoad = new MenuItem("Load as...");
        MenuItem mStop = new MenuItem("Stop");
        Menu mSettings = new Menu("Settings");
        Menu mHelp = new Menu("Help");

        CheckboxMenuItem statisticCheckBox = new CheckboxMenuItem("Show statistic", true);
        CheckboxMenuItem timeCheckBox = new CheckboxMenuItem("Show time", true);

        JButton toolStart = new JButton("1");

        JButton startWarrAiButton = new JButton("Start Warrior Ai");
        JButton stopWarrAiButton = new JButton("Stop Warrior Ai");

        JButton startWorkAiButton = new JButton("Start Worker Ai");
        JButton stopWorkAiButton = new JButton("Stop Worker Ai");

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
                                start(window, stopButton, startButton, toolStart, toolStop, showButton, n1, n2, t1, t2, p1, p2, warrPriority, workPriority);

                            } else {
                                JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);
                            }
                        }
                        break;
                    case KeyEvent.VK_E:
                        if (firstTimeRun) {
                            stop(window, showInfo, stopButton, startButton, toolStart, showButton, toolStop, labelTime, stopWarrAiButton, stopWorkAiButton, startWarrAiButton, startWorkAiButton);

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

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                FileWriter fos = null;
                try {
                    fos = new FileWriter("src/res/config.txt");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    String str = n1.getText() + ' ' + n2.getText() + ' ' + p1.getSelectedIndex() + ' ' + p2.getSelectedIndex() + ' ' + t1.getText() + ' ' + t2.getText() + ' ' + warrPriority.getSelectedIndex() + ' ' + workPriority.getSelectedIndex() + ' ' + -1;
                    fos.write(str);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                FileReader fis = null;
                try {
                    fis = new FileReader("src/res/config.txt");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                try {
                    String buf = null;
                    char[] buf1 = new char[128];
                    int c;
                    int i = 0;
                    while ((c = fis.read()) != -1) {
                        buf1[i] = (char) c;
                        i++;
                    }
                    buf = new String(buf1);
                    System.out.println(buf);
                    String[] str = buf.split(" ");
                    n1.setText(str[0]);
                    n2.setText(str[1]);
                    p1.setSelectedIndex(Integer.parseInt(str[2]));
                    p2.setSelectedIndex(Integer.parseInt(str[3]));
                    t1.setText(str[4]);
                    t2.setText(str[5]);
                    warrPriority.setSelectedIndex(Integer.parseInt(str[6]));
                    System.out.println(str[7]);
                    workPriority.setSelectedIndex(Integer.parseInt(str[7]));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });


        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, height - 210, widht, 210);
        buttonPanel.setBackground(new Color(34, 139, 34));

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

        warrPriopityLabel.setBounds(10, 250, 150, 30);
        warrPriopityLabel.setFont(new Font("Arial", Font.BOLD, 12));

        warrPriority.setBounds(120, 250, 50, 30);
        warrPriority.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstTimeRun) {
                    warrAI.setPriority(warrPriority.getSelectedIndex() + 1);
                    System.out.println(warrPriority.getSelectedIndex() + 1);
                }
            }
        });
        workPriopityLabel.setBounds(10, 290, 150, 30);
        workPriopityLabel.setFont(new Font("Arial", Font.BOLD, 12));

        workPriority.setBounds(120, 290, 50, 30);
        workPriority.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstTimeRun) {
                    workAI.setPriority(workPriority.getSelectedIndex() + 1);
                    System.out.println(workPriority.getSelectedIndex() + 1);
                }
            }
        });

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
        mainPanel.setBounds(0, 40, widht - 200, height - 250);
        mainPanel.setLayout(null);
        buttonPanel.add(labelTime);

        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setBounds(widht - 200, 40, 200, height - 250);
        buttonPanel2.setBackground(new Color(34, 139, 34));


        window.add(mainPanel).requestFocus();
        window.add(buttonPanel).requestFocus();

        mStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!firstTimeRun) {
                    System.out.println(checkStr(n1.getText()));
                    if (checkStr(n1.getText()) && checkStr(n2.getText()) && checkStr(t1.getText()) && checkStr(t2.getText())) {
                        start(window, stopButton, startButton, toolStart, toolStop, showButton, n1, n2, t1, t2, p1, p2, warrPriority, workPriority);

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
                    stop(window, showInfo, stopButton, startButton, toolStart, showButton, toolStop, labelTime, stopWarrAiButton, stopWorkAiButton, startWarrAiButton, startWorkAiButton);

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

        mConsole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConsoleRunning) {
                    isConsoleRunning = true;
                    int wConsole = 800, hConsole = 400;
                    JDialog consoleDialog = new JDialog(window, "Consol");
                    consoleDialog.setSize(wConsole, hConsole);


                    JTextArea consoleArea = new JTextArea();
                    consoleArea.setBounds(0, 0, wConsole, hConsole);
                    consoleArea.setFont(new Font("Times new Roman", Font.BOLD, 24));
                    consoleArea.setForeground(Color.white);
                    consoleArea.setBackground(Color.BLACK);

                    consoleDialog.add(new JScrollPane(consoleArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

                    consoleDialog.setVisible(true);
                    consoleArea.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            super.keyPressed(e);
                            if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                                String[] str = consoleArea.getText().split("\n");
                                if (str[str.length - 1].equals("Start") || str[str.length - 1].equals("start")) {
                                    if (!firstTimeRun) {
                                        start(window, stopButton, startButton, toolStart, toolStop, showButton, n1, n2, t1, t2, p1, p2, warrPriority, workPriority);
                                        consoleArea.append("\nComplite!");
                                    }else consoleArea.append("\nError: Simulation is already running!");
                                } else {
                                    if (str[str.length - 1].equals("Stop") || str[str.length - 1].equals("stop")) {
                                        if (firstTimeRun) {
                                            stop(window, showInfo, stopButton, startButton, toolStart, showButton, toolStop, labelTime, stopWarrAiButton, stopWorkAiButton, startWarrAiButton, startWorkAiButton);
                                            consoleArea.append("\nComplite!");

                                        } else consoleArea.append("\nError: Simulation is not running!");
                                    } else {
                                        consoleArea.append("\nError: Unknown command!");
                                    }
                                }
                            }
                        }
                    });
                    consoleDialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            isConsoleRunning = false;
                        }
                    });
                }
            }
        });

        mSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serialization(window);
            }
        });
        mLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deserialization(window);
            }
        });

        mHelp.add(mHelp1);
        mSim.add(mConsole);
        mSim.add(mSave);
        mSim.add(mLoad);
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

        startTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeFromStart++;
                labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
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
                    start(window, stopButton, startButton, toolStart, toolStop, showButton, n1, n2, t1, t2, p1, p2, warrPriority, workPriority);

                } else {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);

                }
            }
        });

        stopButton.setBounds(250, 20, 150, 50);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop(window, showInfo, stopButton, startButton, toolStart, showButton, toolStop, labelTime, stopWarrAiButton, stopWorkAiButton, startWarrAiButton, startWorkAiButton);

            }
        });
        buttonPanel.add(stopButton);

        settingsButton.setBounds(450, 20, 150, 50);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleSettings(n1, n2, p1, p2, t1, t2, n1Label, t2Label, t1Label, n2Label, p1Label, p2Label);
                window.requestFocus();
            }
        });
        buttonPanel.add(settingsButton);

        showButton.setBounds(650, 20, 150, 50);
        showButton.setEnabled(false);
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    warrAI.stopAnimation();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                try {
                    workAI.stopAnimation();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

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
                warrAI.resumeAnimation();
                workAI.resumeAnimation();
            }
        });

        buttonPanel.add(showButton);

        startWarrAiButton.setEnabled(false);
        startWarrAiButton.setBounds(10, 120, 160, 30);
        startWarrAiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                warrAI.resumeAnimation();
                startWarrAiButton.setEnabled(false);
                stopWarrAiButton.setEnabled(true);
            }
        });

        stopWarrAiButton.setBounds(10, 160, 160, 30);
        stopWarrAiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    warrAI.stopAnimation();
                    startWarrAiButton.setEnabled(true);
                    stopWarrAiButton.setEnabled(false);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        startWorkAiButton.setEnabled(false);
        startWorkAiButton.setBounds(10, 10, 160, 30);
        startWorkAiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                workAI.resumeAnimation();
                startWorkAiButton.setEnabled(false);
                stopWorkAiButton.setEnabled(true);
            }
        });

        stopWorkAiButton.setEnabled(true);
        stopWorkAiButton.setBounds(10, 50, 160, 30);
        stopWorkAiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    workAI.stopAnimation();
                    startWorkAiButton.setEnabled(true);
                    stopWorkAiButton.setEnabled(false);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel2.setLayout(null);
        buttonPanel2.add(startWorkAiButton);
        buttonPanel2.add(stopWorkAiButton);
        buttonPanel2.add(startWarrAiButton);
        buttonPanel2.add(stopWarrAiButton);
        buttonPanel2.add(warrPriopityLabel);
        buttonPanel2.add(workPriopityLabel);
        buttonPanel2.add(warrPriority);
        buttonPanel2.add(workPriority);

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
                        start(window, stopButton, startButton, toolStart, toolStop, showButton, n1, n2, t1, t2, p1, p2, warrPriority, workPriority);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);

                    }
                }
            }
        });
        toolStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop(window, showInfo, stopButton, startButton, toolStart, showButton, toolStop, labelTime, stopWarrAiButton, stopWorkAiButton, startWarrAiButton, startWorkAiButton);
            }
        });
        toolSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleSettings(n1, n2, p1, p2, t1, t2, n1Label, t2Label, t1Label, n2Label, p1Label, p2Label);
                window.requestFocus();
            }
        });

        toolBar.add(toolStart);
        toolBar.add(toolStop);
        toolBar.add(toolSettings);
        window.add(buttonPanel2);

        toolBar.setBackground(new Color(2, 112, 255));
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

    private void start(JFrame window, JComponent stopButton, JComponent startButton, JComponent toolStart, JComponent toolStop, JComponent showButton, TextField n1,
                       TextField n2, TextField t1, TextField t2, JComboBox p1, JComboBox p2, JComboBox warrPriority, JComboBox workPriority) {
        System.out.println("Start");
        if (mas.getArray() == null) {
            mas.setArray(new Vector<>());
            mas.setIndef(new HashSet<>());
            mas.setTreeTime(new TreeMap<>());
        }
        startTime.start();

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

        if (workAI == null) {
            warrAI = new WarrAI();
            workAI = new WorkAI();
            warrAI.setHabitat(this);
            workAI.setHabitat(this);
            warrAI.start();
            workAI.start();
            warrAI.setGoing(true);
            workAI.setGoing(true);
        }
        warrAI.setPriority(warrPriority.getSelectedIndex() + 1);
        workAI.setPriority(workPriority.getSelectedIndex() + 1);

        firstTimeRun = true;
    }

    private void stop(JFrame window, Checkbox showInfo, JButton stopButton, JButton startButton, JButton toolStart, JButton showButton, JButton toolStop, JLabel labelTime, JButton stopWarrAiButton, JButton stopWorkAiButton, JButton startWarrAiButton, JButton startWorkAiButton) {
        startTime.stop();
        if (firstTimeRun) {
            startTime.stop();
            int result = 0;
            if (showInfo.getState()) {
                try {
                    warrAI.stopAnimation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    workAI.stopAnimation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = statistic(window);
            }
            if (result == 0) {
                warrAI.setGoing(false);
                workAI.setGoing(false);
                window.repaint();
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                toolStart.setEnabled(true);
                toolStop.setEnabled(false);
                showButton.setEnabled(false);
                if (!stopWarrAiButton.isEnabled()) {
                    stopWarrAiButton.setEnabled(true);
                    startWarrAiButton.setEnabled(false);
                }
                if (!stopWorkAiButton.isEnabled()) {
                    stopWorkAiButton.setEnabled(true);
                    startWorkAiButton.setEnabled(false);
                }

                workerCount = 0;
                warriorCount = 0;
                timeFromStart = 0;
                firstTimeRun = false;
                labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                mas.setArray(null);
                mas.setTreeTime(null);
                mas.setIndef(null);
                workAI = null;
                warrAI = null;

            } else {
                if (startWarrAiButton.isEnabled()) warrAI.resumeAnimation();
                if (startWorkAiButton.isEnabled()) workAI.resumeAnimation();
                startTime.start();

            }
        }
    }

    private int statistic(JFrame window) {
        return JOptionPane.showConfirmDialog(window, new String[]{"Warriors: " + warriorCount, "Workers:" + workerCount, "Sum: " + (warriorCount + workerCount), "Work Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60}, "Statistic", JOptionPane.OK_CANCEL_OPTION);
    }

    private void update(float time, JComponent g) {

        for (int i = 0; i < mas.getArray().size(); i++) {
            if (mas.getArray().get(i).getBornTime() + mas.getArray().get(i).getLifeTime() <= (int) time) {

                mas.getIndef().remove(mas.getArray().get(i).getIndef());
                mas.getTreeTime().remove(mas.getArray().get(i).getIndef());
                mas.getArray().remove(i);
                System.out.println("NUUU");

            }
        }
        if (time % N1 == 0) {
            if (Math.random() < P1) {
                mas.getArray().add(antFactory.createWarrior((float) (Math.random() * (widht - 300 + 1)), (float) Math.random() * (height - 490) + 40, lifeWarr, (int) time, warriorCount + workerCount));
                mas.getIndef().add(mas.getArray().get(mas.getArray().size() - 1).getIndef());
                mas.getTreeTime().put(mas.getArray().get(mas.getArray().size() - 1).getIndef(), mas.getArray().get(mas.getArray().size() - 1).getBornTime());
                System.out.println("WarriorCreate(" + mas.getArray().get(mas.getArray().size() - 1).getX() + "," + mas.getArray().get(mas.getArray().size() - 1).getY() + ")");
                warriorCount++;
            }
        }
        if (time % N2 == 0) {
            if (Math.random() < P2) {
                mas.getArray().add(antFactory.createWorker((float) (Math.random() * (widht - 300 + 1)), (float) Math.random() * (height - 490) + 40, lifeWork, (int) time, warriorCount + workerCount));
                mas.getIndef().add(mas.getArray().get(mas.getArray().size() - 1).getIndef());
                mas.getTreeTime().put(mas.getArray().get(mas.getArray().size() - 1).getIndef(), mas.getArray().get(mas.getArray().size() - 1).getBornTime());
                System.out.println("WorkerCreate(" + mas.getArray().get(mas.getArray().size() - 1).getX() + "," + mas.getArray().get(mas.getArray().size() - 1).getY() + ")");
                workerCount++;
            }
        }
        draw(g);
    }

    private void setVisibleSettings(TextField n1, TextField n2, JComponent p1, JComponent p2, TextField t1, TextField t2, JComponent n1Label, JComponent t2Label,
                                    JComponent t1Label, JComponent n2Label, JComponent p1Label, JComponent p2Label) {
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

    public synchronized void draw(JComponent g) {
        int w = g.getWidth(), h = g.getHeight();
        BufferedImage fieldImage;
        fieldImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics fieldImageGraphics = fieldImage.getGraphics();
        fieldImageGraphics.drawImage(backGround, 0, 0, null);
        for (int i = 0; i < mas.getArray().size(); i++)
            fieldImageGraphics.drawImage(mas.getArray().get(i).getImage(), (int) mas.getArray().get(i).getX(), (int) mas.getArray().get(i).getY(), 100, 200, null);

        g.getGraphics().drawImage(fieldImage, 0, 0, w, h, null);

    }

    private void serialization(JFrame window) {
        try {
            if (firstTimeRun) {
                startTime.stop();
                if (!workAI.isStopped) workAI.stopAnimation();
                if (!warrAI.isStopped) warrAI.stopAnimation();
            }

            JFileChooser fileChooser = new JFileChooser(new File("src/res"));
            fileChooser.setDialogTitle("Choose your file");
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            File saveFile = null;
            int result = fileChooser.showSaveDialog(window);
            if (result == JFileChooser.APPROVE_OPTION) {
                saveFile = fileChooser.getSelectedFile();
            } else throw (new Exception());

            FileOutputStream outputStream = new FileOutputStream(saveFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(mas);
            objectOutputStream.close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstTimeRun) {
            startTime.start();
            workAI.resumeAnimation();
            warrAI.resumeAnimation();
        }
    }

    private void deserialization(JFrame window) {
        try {
            if (firstTimeRun) {
                startTime.stop();
                if (!workAI.isStopped) workAI.stopAnimation();
                if (!warrAI.isStopped) warrAI.stopAnimation();
            }

            JFileChooser fileChooser = new JFileChooser(new File("src/res"));
            fileChooser.setDialogTitle("Choose your file");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            File loadFile = null;
            int result = fileChooser.showSaveDialog(window);
            if (result == JFileChooser.APPROVE_OPTION) {
                loadFile = fileChooser.getSelectedFile();
            } else throw (new Exception());

            FileInputStream fileInputStream = new FileInputStream(loadFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mas = (Singleton) objectInputStream.readObject();

            for (int i = 0; i < mas.getArray().size(); i++) {
                mas.getArray().get(i).setBornTime((int) timeFromStart);
                System.out.println(mas.getArray().get(i).getIndef());
            }
            objectInputStream.close();

            if (firstTimeRun) {
                startTime.start();
                workAI.resumeAnimation();
                warrAI.resumeAnimation();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
