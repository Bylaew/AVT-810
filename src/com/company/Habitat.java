package com.company;
import javafx.scene.layout.Background;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;
import javax.swing.event.*;

public class Habitat {
    private int NumberRabbits=0;
    private Singleton obj=Singleton.getInstance();
    private JFrame frame;
    private Timer mTimer = new Timer();
    private int NumberAlbino;
    private int CommonRabbit;
    private boolean ShowTime=false;
    private boolean simulate=false;
    private boolean JustStart=true;
    private long currentTime;
    private JPanel panel,panelTwo;
    private double N1=400;
    private double N2=500;
    private float k= (float) 0.1;
    private double P1=0.3;
    private long to=0;
    private boolean bol=false;
    JMenuBar menuBar=new JMenuBar();

    public Habitat() {

        Panel();
        //new Massage();
        panelTwo.setLayout(null);
        frame=new JFrame("Window");
        frame.add(panel,BorderLayout.CENTER);
        frame.add(panelTwo,BorderLayout.EAST);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();
        Keys();


    }

    public void Keys()
    {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_B&&!simulate)
                {
                    JustStart=false;
                    Start();
                }
                if(e.getKeyCode()==KeyEvent.VK_E&&simulate)
                {
                    Stop();
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
        panel=new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (JustStart) {
                    g.setFont(new Font("Times Roman",Font.BOLD,20));
                    g.drawString("Press Key B or Button Start on begin",250,250);
                }
                else {
                    if (simulate) {
                        for (int i = 0; i < obj.GetArray().size(); i++) {

                            try {
                                g.drawImage(obj.GetArray().get(i).getImage(),obj.GetArray().get(i).getX(),obj.GetArray().get(i).getY(),null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (ShowTime) {
                            g.setColor(Color.MAGENTA);
                            g.setFont(new Font("Times Roman", Font.BOLD, 15));
                            g.drawString("Simulation " + currentTime + "ms", 0, 530);
                        }
                    } else {
                        g.setColor(Color.RED);
                        g.setFont(new Font("Courier", Font.ITALIC + Font.BOLD, 15));
                        g.drawString("Simulation is running for " + currentTime + "ms", 380, 270);
                        g.setColor(Color.darkGray);
                        g.drawString("ALL Rabbits " + NumberRabbits, 380, 250);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Helvetica", Font.PLAIN + Font.BOLD, 15));
                        g.drawString("Albino rabbit " + NumberAlbino, 380, 220);
                        g.setColor(Color.BLUE);
                        g.drawString("Common rabbit " + CommonRabbit, 380, 235);
                    }
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                panelTwo.requestFocus();
            }
        });

        panelTwo=new JPanel();
        Buttons();
        chekBox();
        listertr();
        KomboBox();
        Texti();
        Meniu();
    }

    public void Buttons() {
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("End");
        startButton.setFocusable(false);
        stopButton.setFocusable(false);
        panelTwo.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!simulate)
                    Start();
            }
        });
        panelTwo.add(stopButton);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { Stop(); }
        });

        startButton.setBounds(5,5,80,30);
        stopButton.setBounds(90,5,80,30);
    }

    public void chekBox() {
        ButtonGroup currentT=new ButtonGroup();
        JRadioButton on=new JRadioButton("Visible time");
        JRadioButton off=new JRadioButton("Not Visible time");
        currentT.add(on);
        currentT.add(off);
        on.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                ShowTime=true;
                frame.repaint();
            }
        });

        off.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                ShowTime=false;
                frame.repaint();
            }
        });
        on.setBounds(5,40,90,30);
        off.setBounds(5,70,120,30);
        on.setFocusable(false);
        off.setFocusable(false);
        panelTwo.add(on);
        panelTwo.add(off);

        JCheckBox boxInformation=new JCheckBox("Info");
        boxInformation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(bol) {
                    // new Massage();
                    //to = currentTime;
                   // mTimer.cancel();
                    bol = false;
                    frame.requestFocus();
                }else
                    bol=true;
                frame.requestFocus();
            }

        });

        boxInformation.setBounds(105,40,90,30);
        boxInformation.setFocusable(false);
        panelTwo.add(boxInformation);
        panelTwo.setPreferredSize(new Dimension(200, 100));
    }

    public void listertr(){
        JList Present= new JList();
        Vector<String> obj=new Vector<String>();
        obj.add("0%");
        obj.add("10%");
        obj.add("20%");
        obj.add("30%");
        obj.add("40%");
        obj.add("50%");
        obj.add("60%");
        obj.add("70%");
        obj.add("80%");
        obj.add("90%");
        obj.add("100%");
        Present.setListData(obj);
        Present.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                JList l=(JList)listSelectionEvent.getSource();
                k=(float)l.getSelectedIndex()/10;
                System.out.println(k);
            }
        });
        Present.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JLabel labelPercent=new JLabel("Percent Albino");
        Present.setSelectedIndex(1);
        labelPercent.setBounds(5,110,70,25);
        Present.setBounds(5,130,35,200);
        panelTwo.add(labelPercent);
        panelTwo.add(Present);
    }

    public void KomboBox()
    {
        JComboBox cbProbability=new JComboBox();
        cbProbability.addItem("0.0");
        cbProbability.addItem("0.1");
        cbProbability.addItem("0.2");
        cbProbability.addItem("0.3");
        cbProbability.addItem("0.4");
        cbProbability.addItem("0.5");
        cbProbability.addItem("0.6");
        cbProbability.addItem("0.7");
        cbProbability.addItem("0.8");
        cbProbability.addItem("0.9");
        cbProbability.addItem("1.0");
        cbProbability.setSelectedIndex(3);
        cbProbability.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                JComboBox bx=(JComboBox)itemEvent.getSource();
                P1= (double) bx.getSelectedIndex()/10;
                System.out.println(P1);
            }
        });
        JLabel labelCb=new JLabel("Probability rabbit");
        labelCb.setBounds(70,110,100,20);
        cbProbability.setBounds(70,130,80,30);
        panelTwo.add(labelCb);
        panelTwo.add(cbProbability);
    }

    public void Texti()
    {
        TextField text=new TextField("400");
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    try {
                        N1=Integer.parseInt(text.getText());
                        System.out.println(N1);
                    }
                    catch (NumberFormatException eg) {
                        new WindowText();
                        N1 = 400;
                    }
                    finally {
                        frame.requestFocus();
                    }
                }
            }});
        JLabel labelRabbit=new JLabel("Time create rabbit");
        labelRabbit.setBounds(70,160,110,20);
        text.setBounds(70,180,120,20);
        panelTwo.add(labelRabbit);
        panelTwo.add(text);


        TextField textTwo=new TextField("500");
        textTwo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    try {
                        N2=Integer.parseInt(textTwo.getText());
                    }
                    catch (NumberFormatException eg)
                    {
                        new WindowText();
                        N2=500;
                    }
                    finally {
                        frame.requestFocus();
                    }
                }
            }
        });
        JLabel label=new JLabel("Time create Albino rabbit");
        textTwo.setBounds(70,220,120,20);
        label.setBounds(70,200,120,20);
        panelTwo.add(label);

        panelTwo.add(textTwo);
        panelTwo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                panelTwo.requestFocus();
            }
        });
    }

public void Meniu()
{
    JMenu Menu=new JMenu("Menu");
    JMenuItem StopMenu=new JMenuItem("Stop");
    JMenuItem StarMenu=new JMenuItem("Start");
    JRadioButtonMenuItem one=new JRadioButtonMenuItem("on");
    JRadioButtonMenuItem two=new JRadioButtonMenuItem("off");
    Menu.add(StarMenu);
    Menu.add(StopMenu);
    Menu.add(one);
    Menu.add(two);

    StarMenu.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(!simulate)
                Start();
            frame.requestFocus();
        }
    });
    StopMenu.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Stop();
            frame.requestFocus();
        }
    });
    one.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ShowTime=true;
            frame.repaint();
            frame.requestFocus();
        }
    });
    two.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ShowTime=false;
            frame.repaint();
            frame.requestFocus();
        }
    });
    menuBar.add(Menu);
}

   public class Massage extends JDialog
    {

        public Massage() {
            JButton button1 = new JButton("OK");
            button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //симуляци останавливается
                    Stop();
                    dispose();
                    requestFocus();
                }
            });
            setTitle("Info");
            JButton button2 = new JButton("cancellation");
            button2.addActionListener(new ActionListener() {
                //Продолжается
                public void actionPerformed(ActionEvent e) {
                    resume(to);
                    dispose();
                    requestFocus();
                }
            });
            // Создание панели содержимого с размещением кнопок
            JPanel contents = new JPanel();
            contents.add(button1);
            contents.add(button2);
            setContentPane(contents);
            JTextArea textArea=new JTextArea(5,25);
            textArea.setFont(new Font("Window", Font.BOLD, 14));
            textArea.setText("Albino Rabbit "+NumberAlbino+"\n"+"Rabbit "+CommonRabbit+"\n"+"ALL Rabbits "+NumberRabbits+"\n"+"Simulation is running for " + currentTime + "ms");
            textArea.setEnabled(false);
            textArea.setBackground(Color.BLACK);
            contents.add(textArea);
            setSize(350, 200);
            setVisible(true);
            requestFocus();
            }
    }


    public class WindowText extends JDialog
    {
        public WindowText() {

            JButton button1 = new JButton("OK");
            button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    requestFocus();
                }
            });
            setTitle("Warning");
            TextArea textArea1=new TextArea(5,30);
            textArea1.setText("This line can only contain numbers"+"\n"+" click 'OK' to continue");
            textArea1.setFont(new Font("Window", Font.BOLD, 14));
            textArea1.setEnabled(false);
            textArea1.setForeground(Color.BLACK);

            JPanel contents = new JPanel();
            contents.add(button1);
            contents.add(textArea1);
            setContentPane(contents);
            frame.requestFocus();
            setSize(350, 200);
            setVisible(true);
            requestFocus();

        }

    }


    private class Updater extends TimerTask{
        private Habitat mHabitat;
        private boolean FirstRun=true;

        public Updater(Habitat habitat)
        {
            mHabitat=habitat;
        }

        public Updater(ActionListener actionListener) {
        }

        @Override
        public void run() {
                currentTime +=100;
            mHabitat.Update(currentTime);
        }
    }

    public void resume(long t)
    {
        simulate=true;
        obj=Singleton.getInstance();
        mTimer.cancel();
        mTimer=new Timer();
        currentTime=t;
        mTimer.schedule(new Updater(this),0,100);
        frame.repaint();
    }

    public void Start()
    {
        if (JustStart)
        JustStart = false;
        mTimer.cancel();
        obj.refreshArray();
        mTimer=new Timer();
        NumberRabbits=0;
        currentTime=0;
        CommonRabbit=0;
        NumberAlbino=0;
        simulate=true;
        mTimer.schedule(new Updater(this),0,100);
    }

    public void Stop()
    {
        mTimer.cancel();
        if(bol&&simulate)
        new Massage();
        simulate=false;
        frame.repaint();
    }


    public void Update(double elapseTime)
    {
        Random r=new Random();
        ConcreteFactory obj1 = new ConcreteFactory();
        if(elapseTime%N2==0 && NumberAlbino<=k*NumberRabbits) {
            obj.GetArray().add(obj1.createAlbinoRabbit());
            NumberRabbits++;
            NumberAlbino++;
        }
        if(elapseTime%N1==0 && r.nextInt(1)<P1) {
            obj.GetArray().add(obj1.createRabbit());
            NumberRabbits++;
             CommonRabbit++;
        }
        frame.repaint();
    }

}