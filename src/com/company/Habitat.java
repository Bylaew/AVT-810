package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    boolean isVisibleSettings = false;

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

    public void init() {
        JFrame window = new JFrame("Muravei)");
        String[] a = {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
        JComboBox p1 = new JComboBox(a);
        JComboBox p2 = new JComboBox(a);
        TextField n1 = new TextField("2.");
        TextField n2 = new TextField("3.");
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JLabel labelTime = new JLabel();
        CheckboxGroup infoGroup = new CheckboxGroup();
        Checkbox showTime = new Checkbox("Показывать время симуляции", infoGroup, true);
        Checkbox show_Time = new Checkbox("Скрыть время симуляции", infoGroup, false);
        Checkbox showInfo = new Checkbox("Показывать информацию", true);
        JToolBar toolBar = new JToolBar();
        JPanel mainPanel = new JPanel();

        MenuBar menu = new MenuBar();
        Menu mSim = new Menu("Simulation");
        MenuItem mStart = new MenuItem("Start");
        MenuItem mStop = new MenuItem("Stop");
        Menu mSettings = new Menu("settings");
        CheckboxMenuItem statisticCheckBox = new CheckboxMenuItem("Show statistic", true);
        CheckboxMenuItem timeCheckBox = new CheckboxMenuItem("Show time", true);
        JButton settingsButton = new JButton("Settings");

        JButton toolStart = new JButton("1");
        JButton toolStop = new JButton("2");
        JButton toolSettings = new JButton("3");

        window.setSize(widht, height);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocation(400, 100);
        window.setLayout(null);

        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, height - 210, widht, 210);
        buttonPanel.setBackground(Color.CYAN);

        labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
        labelTime.setFont(new Font("TimesRoman", Font.ITALIC, 40));
        labelTime.setBounds(1000, 100, 100, 40);
        labelTime.setForeground(Color.blue);
        labelTime.setVisible(true);

        showTime.setBounds(700, 20, 200, 20);
        show_Time.setBounds(700, 50, 200, 20);
        showInfo.setBounds(900, 20, 200, 20);
        buttonPanel.add(showInfo);
        buttonPanel.add(showTime);
        buttonPanel.add(show_Time);

        p1.setBounds(100, 100, 150, 30);
        n1.setBounds(300, 100, 150, 30);
        p2.setBounds(500, 100, 150, 30);
        n2.setBounds(700, 100, 150, 30);
        n1.setVisible(false);
        n2.setVisible(false);
        p1.setVisible(false);
        p2.setVisible(false);
        buttonPanel.add(p1);
        buttonPanel.add(p2);
        buttonPanel.add(n1);
        buttonPanel.add(n2);

        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBounds(0, 40, widht, height - 250);
        mainPanel.setLayout(null);
        mainPanel.add(labelTime);

        window.add(mainPanel);
        window.add(buttonPanel);

        mStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!firstTimeRun) {
                    System.out.println(checkStr(n1.getText()));
                    if (checkStr(n1.getText()) && checkStr(n2.getText())) {
                        stopButton.setEnabled(true);
                        startButton.setEnabled(false);
                        N1 = Float.parseFloat(n1.getText());
                        N2 = Float.parseFloat(n2.getText());
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
                        workerCount = 0;
                        warriorCount = 0;
                        timeFromStart = 0;
                        firstTimeRun = false;
                        labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                        mas = null;
                    } else {
                        startTime.start();

                    }
                }
            }
        });
        menu.add(mSim);
        menu.add(mSettings);

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

        startTime = new

                Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeFromStart++;
                labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                update(timeFromStart, mainPanel.getGraphics());
            }
        });

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        if (!firstTimeRun) {
                            System.out.println(checkStr(n1.getText()));
                            if (checkStr(n1.getText()) && checkStr(n2.getText())) {
                                stopButton.setEnabled(true);
                                startButton.setEnabled(false);
                                N1 = Float.parseFloat(n1.getText());
                                N2 = Float.parseFloat(n2.getText());
                                P1 = ((float) (p1.getSelectedIndex() + 1)) / 10;
                                P2 = ((float) (p2.getSelectedIndex() + 1)) / 10;

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
                                workerCount = 0;
                                warriorCount = 0;
                                timeFromStart = 0;
                                firstTimeRun = false;
                                labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                                mas = null;
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

        startButton.setBounds(100, 20, 150, 50);
        stopButton.setEnabled(false);
        buttonPanel.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(checkStr(n1.getText()));
                if (checkStr(n1.getText()) && checkStr(n2.getText())) {
                    stopButton.setEnabled(true);
                    startButton.setEnabled(false);
                    toolStart.setEnabled(false);
                    toolStop.setEnabled(true);
                    N1 = Float.parseFloat(n1.getText());
                    N2 = Float.parseFloat(n2.getText());
                    P1 = ((float) (p1.getSelectedIndex() + 1)) / 10;
                    P2 = ((float) (p2.getSelectedIndex() + 1)) / 10;

                    firstTimeRun = true;
                    start(window);
                } else {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода. Введие числа", "Error", JOptionPane.OK_OPTION);

                }
            }
        });

        stopButton.setBounds(300, 20, 150, 50);
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
                    workerCount = 0;
                    warriorCount = 0;
                    timeFromStart = 0;
                    labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                    firstTimeRun = false;
                    mas = null;
                } else {
                    startTime.start();

                }
            }
        });
        buttonPanel.add(stopButton);

        settingsButton.setBounds(500, 20, 150, 50);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isVisibleSettings) {
                    n1.setVisible(false);
                    n2.setVisible(false);
                    p1.setVisible(false);
                    p2.setVisible(false);
                    isVisibleSettings = false;
                } else {
                    n1.setVisible(true);
                    n2.setVisible(true);
                    p1.setVisible(true);
                    p2.setVisible(true);
                    isVisibleSettings = true;
                }
            }
        });
        buttonPanel.add(settingsButton);

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
                    if (checkStr(n1.getText()) && checkStr(n2.getText())) {
                        stopButton.setEnabled(true);
                        startButton.setEnabled(false);
                        toolStart.setEnabled(false);
                        toolStop.setEnabled(true);
                        N1 = Float.parseFloat(n1.getText());
                        N2 = Float.parseFloat(n2.getText());
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
                    workerCount = 0;
                    warriorCount = 0;
                    timeFromStart = 0;
                    firstTimeRun = false;
                    labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
                    mas = null;
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
                    isVisibleSettings = false;
                } else {
                    n1.setVisible(true);
                    n2.setVisible(true);
                    p1.setVisible(true);
                    p2.setVisible(true);
                    isVisibleSettings = true;
                }
            }
        });
        toolBar.add(toolStart);
        toolBar.add(toolStop);
        toolBar.add(toolSettings);
        toolBar.setBackground(Color.YELLOW);
        window.add(toolBar);

        window.setVisible(true);
    }

    private void start(JFrame window) {
        System.out.println("Start");
        mas = new ArrayList<Ant>();
        startTime.start();
    }

    private int statistic(JFrame window) {
        return JOptionPane.showConfirmDialog(window, new String[]{"Warriors: " + warriorCount, "Workers:" + workerCount, "Sum: " + (warriorCount + workerCount), "Work Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60}, "Statistic", JOptionPane.OK_CANCEL_OPTION);

        /*System.out.println("Statistic");
        JDialog dialog = new JDialog(window, "Statistic");
        dialog.setLayout(null);

        JButton okButton = new JButton("Ok");
        okButton.setBounds(100, 300, 500, 500);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);

            }
        });

        JButton canselButton = new JButton("Cansel");
        canselButton.setBounds(300, 300, 100, 100);
        canselButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                startTime.start();
            }
        });
        dialog.add(okButton);
        dialog.add(canselButton);

        JTextArea dialogLabel1 = new JTextArea("Warriors: " + warriorCount);
        JTextArea dialogLabel2 = new JTextArea("Workers:" + workerCount);
        JTextArea dialogLabel3 = new JTextArea("Sum: " + (warriorCount + workerCount));
        JTextArea dialogLabel4 = new JTextArea("Work Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);

        dialogLabel1.setFont(new Font("Arial", Font.BOLD, 60));
        dialogLabel1.setForeground(Color.cyan);
        dialogLabel1.setEditable(false);

        dialogLabel2.setFont(new Font("TimesRoman", Font.BOLD, 12));
        dialogLabel2.setForeground(Color.BLUE);
        dialogLabel2.setEditable(false);

        dialogLabel3.setFont(new Font("Callibri", Font.BOLD, 32));
        dialogLabel3.setForeground(Color.black);
        dialogLabel3.setEditable(false);

        dialogLabel4.setFont(new Font("Courier", Font.BOLD, 40));
        dialogLabel4.setForeground(Color.MAGENTA);
        dialogLabel4.setEditable(false);

        dialog.setLayout(new GridLayout(4, 1));

        dialog.add(dialogLabel1);
        dialog.add(dialogLabel2);
        dialog.add(dialogLabel3);
        dialog.add(dialogLabel4);

        dialog.setSize(500, 500);

        dialog.setVisible(true);
        System.out.println("123");*/
    }

    private void update(float time, Graphics g) {
        if (time % N1 == 0) {
            if (Math.random() < P1) {
                mas.add(antFactory.createWarrior((float) (Math.random() * (widht - 100 + 1)), (float) Math.random() * (height - 490) + 40));
                System.out.println("WarriorCreate(" + mas.get(mas.size() - 1).getX() + "," + mas.get(mas.size() - 1).getY() + ")");
                for (Ant a : mas)
                    g.drawImage(a.getImage(), (int) a.getX(), (int) a.getY(), 100, 200, null);
                warriorCount++;
            }
        }
        if (time % N2 == 0) {
            if (Math.random() < P2) {
                mas.add(antFactory.createWorker((float) (Math.random() * (widht - 100 + 1)), (float) Math.random() * (height - 490) + 40));
                System.out.println("WorkerCreate(" + mas.get(mas.size() - 1).getX() + "," + mas.get(mas.size() - 1).getY() + ")");
                for (Ant a : mas)
                    g.drawImage(a.getImage(), (int) a.getX(), (int) a.getY(), 100, 200, null);
                workerCount++;
            }
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

