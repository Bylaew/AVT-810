package com.company;

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
    public long currentTime;
    private JPanel panel,panelTwo;
    public double N1=400;
    private double N2=500;
    private int lifeTimeRabbit=1000;
    private int lifeTimeAlbino=10000;
    private float k= (float) 0.1;
    private double P1=0.3;
    private long to=0;
    private boolean bol=false;
    private boolean go=false;//не дает книпоке currentObj продолжить после остановления симуляции
    private ConcreteFactory concrete;
    private JMenuBar menuBar=new JMenuBar();
    private BaceAICommon baceAICommon=new BaceAICommon(this);
    private BaceAIAlbino  baceAIAlbino=new BaceAIAlbino(this);
     boolean ready=true;
     boolean readyAlbino=true;

    public Habitat() {

        Panel();
        panelTwo.setLayout(null);
        frame=new JFrame("Window");
        frame.add(panel,BorderLayout.CENTER);
        frame.add(panelTwo,BorderLayout.EAST);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Keys();
        frame.requestFocus();
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
                    }
                    else
                    {
                        ShowTime=true;
                    }
                    frame.repaint();
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
                        for (int i = 0; i < obj.GetVector().size(); i++) {
                            try {
                                g.drawImage(obj.GetVector().get(i).getImage(), obj.GetVector().get(i).getX(), obj.GetVector().get(i).getY(), null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (ShowTime) {
                            g.setColor(Color.MAGENTA);
                            g.setFont(new Font("Times Roman", Font.BOLD, 15));
                            g.drawString("Simulation " + currentTime + "ms", 0, 530);
                        }
                    }
                    else {
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
                frame.requestFocus();
            }
        });

        panelTwo=new JPanel();
        Buttons();
        chekBox();
        listertr();
        KomboBox();
        Texti();
        Meniu();
        baceAICommon= new BaceAICommon(this);
        baceAIAlbino=new BaceAIAlbino(this);
        panelTwo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.requestFocus();
            }
        });
    }


    public void Buttons() {
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("End");
        stopButton.setFocusable(false);
        startButton.setFocusable(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!simulate)
                    Start();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { Stop(); frame.requestFocus();}
        });
        panelTwo.add(startButton);
        panelTwo.add(stopButton);
        startButton.setBounds(5,5,80,30);
        stopButton.setBounds(90,5,80,30);

        JButton currentObj=new JButton("Current obj");
        currentObj.setFocusable(false);
        currentObj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                pause(currentTime);
                getCurrentObj();
                currentObj.requestFocus();
            }
        });

        panelTwo.add(currentObj);
        currentObj.setBounds(5,340,180,20);
        //Habbitat
        JButton buttonWait=new JButton("Wait Rabbit");
        buttonWait.setFocusable(false);
        buttonWait.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                ready=false;
            }
        });
        panelTwo.add(buttonWait);
        buttonWait.setBounds(5,370,100,25);

        JButton buttonNotify=new JButton("notify Rabbit");
        buttonNotify.setFocusable(false);
        buttonNotify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                synchronized (obj.GetVector())
                {
                    ready=true;
                    obj.GetVector().notify();
                }
            }
        });
        panelTwo.add(buttonNotify);
        buttonNotify.setBounds(5,400,100,25);

        JButton buttonWaitAlb=new JButton("Wait Albino");
        buttonWaitAlb.setFocusable(false);
        buttonWaitAlb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                readyAlbino=false;
            }
        });
        panelTwo.add(buttonWaitAlb);
        buttonWaitAlb.setBounds(5,430,100,25);
        JButton buttonNoti=new JButton("notify Albino");
        buttonNoti.setFocusable(false);
        buttonNoti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                synchronized (obj.GetVector())
                {
                    obj.GetVector().notify();
                    readyAlbino=true;
                }
            }
        });
        panelTwo.add(buttonNoti);
        buttonNoti.setBounds(5,460,100,25);
    }


    public void chekBox() {
        ButtonGroup currentT=new ButtonGroup();
        JRadioButton on=new JRadioButton("Visible time");
        JRadioButton off=new JRadioButton("Not Visible time");
        on.setFocusable(false);
        off.setFocusable(false);
        currentT.add(on);
        currentT.add(off);
        on.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                ShowTime=true;
            }
        });
        off.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                ShowTime=false;
            }
        });
        on.setBounds(5,40,90,30);
        off.setBounds(5,70,120,30);
        panelTwo.add(on);
        panelTwo.add(off);

        JCheckBox boxInformation=new JCheckBox("Info");
        boxInformation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(bol) {
                    bol = false;
                }else
                    bol=true;
            }
        });
        boxInformation.setBounds(105,40,90,30);
        boxInformation.setFocusable(false);
        panelTwo.add(boxInformation);
        panelTwo.setPreferredSize(new Dimension(200, 100));
    }

    public void listertr(){
        JList Present= new JList();
        Vector<String> obj=new Vector();
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
        Present.setRequestFocusEnabled(false);
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
        cbProbability.setRequestFocusEnabled(false);
        JLabel labelCb=new JLabel("Probability rabbit");
        labelCb.setBounds(70,110,100,20);
        cbProbability.setBounds(70,130,80,30);
        panelTwo.add(labelCb);
        panelTwo.add(cbProbability);

        JComboBox BOXrabbit=new JComboBox();
        BOXrabbit.addItem("1");
        BOXrabbit.addItem("2");
        BOXrabbit.setSelectedIndex(0);
        BOXrabbit.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                JComboBox box=(JComboBox)itemEvent.getSource();
                if(box.getSelectedIndex()==0) {
                   // System.out.println(box);
                    new Thread(baceAICommon).setPriority(Thread.MAX_PRIORITY);
                }
                if(box.getSelectedIndex()==1) {
                    new Thread(baceAICommon).setPriority(Thread.MIN_PRIORITY);
                }
            }
        });
        BOXrabbit.setFocusable(false);
        BOXrabbit.setBounds(120,380,70,25);
        JLabel label=new JLabel("Rabbit priority");
        label.setBounds(120,360,80,25);
        panelTwo.add(label);
        panelTwo.add(BOXrabbit);


        JComboBox BOXalbino=new JComboBox();
        BOXalbino.addItem("1");
        BOXalbino.addItem("2");
        BOXalbino.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                JComboBox box=(JComboBox)itemEvent.getSource();
                if(box.getSelectedIndex()==0) {
                    new Thread(baceAICommon).setPriority(Thread.MAX_PRIORITY);
                }
                if(box.getSelectedIndex()==1) {
                    new Thread(baceAICommon).setPriority(Thread.MIN_PRIORITY);
                }
            }
        });
        BOXalbino.setFocusable(false);
        BOXalbino.setBounds(120,420,70,25);
        JLabel labelAl=new JLabel("Albino priority");
        labelAl.setBounds(120,400,80,25);
        panelTwo.add(labelAl);
        panelTwo.add(BOXalbino);
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
                        getWindowText();
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
                        getWindowText();
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

        TextField LifeTextRabbit = new TextField();
        LifeTextRabbit.setText("1000");
        LifeTextRabbit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    try{
                        lifeTimeRabbit=Integer.parseInt(LifeTextRabbit.getText());
                    }catch (NumberFormatException ex){
                        getWindowText();
                      lifeTimeRabbit =1000;
                    }
                    finally {
                        frame.requestFocus();
                    }
                }
            }
        });
        JLabel labelRab=new JLabel("life Time Rabbit");
        labelRab.setBounds(70,240,120,20);
        panelTwo.add(labelRab);
        panelTwo.add(LifeTextRabbit);
        LifeTextRabbit.setBounds(70,260,120,20);

        TextField LifeTextAlbino=new TextField("10000");
        LifeTextAlbino.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    try{
                        lifeTimeAlbino=Integer.parseInt(LifeTextAlbino.getText());
                    }catch (NumberFormatException ex){
                        getWindowText();
                       lifeTimeAlbino=10000;
                    }
                    finally {
                        frame.requestFocus();
                    }
                }
            }
        });
        JLabel labelAlb=new JLabel("life Time Albino");
        labelAlb.setBounds(70,280,120,20);
        panelTwo.add(labelAlb);
        panelTwo.add(LifeTextAlbino);
        LifeTextAlbino.setBounds(70,300,120,20);
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
            }
        });
        StopMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Stop();
            }
        });
        one.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ShowTime=true;
                frame.repaint();
            }
        });
        two.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ShowTime=false;
                frame.repaint();
            }
        });
        Menu.setFocusable(false);
        menuBar.setFocusable(false);
        menuBar.add(Menu);
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

    public void pause(long t)
    {
        currentTime=t;
        mTimer.cancel();
        mTimer=new Timer();
    }

    public void resume(long t)
    {
        simulate=true;
        mTimer.cancel();
        mTimer=new Timer();
        currentTime=t;
        mTimer.schedule(new Updater(this),0,100);
    }

    public void Start()
    {
        concrete= new ConcreteFactory();
        new Thread(baceAICommon).start();
        new Thread(baceAIAlbino).start();
        if (JustStart)
            JustStart = false;
        mTimer.cancel();
        obj.refreshMap();
        obj.refreshID();
        obj.refreshVector();
        mTimer=new Timer();
        NumberRabbits=0;
        currentTime=0;
        CommonRabbit=0;
        NumberAlbino=0;
        simulate=true;
        go=true;
        mTimer.schedule(new Updater(this),0,100);
    }

    public void Stop()
    {
        //baceAICommon.stopped();
        //baceAIAlbino.stopped();
        mTimer.cancel();
        if(bol&&simulate)
            getMessage();
        go=false;
        simulate=false;
        frame.repaint();

    }
    public void getMessage(){
        new Message();
    }
    public void getCurrentObj(){
        new currentObject();
    }
    public void getWindowText()
    {
        new WindowText();
    }
    public class Message extends JDialog
    {

        public Message() {
            JButton button1 = new JButton("OK");
            button1.setFocusable(false);
            button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    requestFocus();
                }
            });
            setTitle("Info");
            JButton button2 = new JButton("cancellation");
            button2.setFocusable(false);
            button2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resume(to);
                    dispose();
                    requestFocus();
                }
            });
            JPanel contents = new JPanel();
            contents.add(button1);
            contents.add(button2);
            setContentPane(contents);
            JTextArea textArea=new JTextArea(5,25);
            textArea.setFont(new Font("Window", Font.BOLD, 14));
            textArea.setText("Albino Rabbit "+NumberAlbino+"\n"+"Rabbit "+CommonRabbit+"\n"+"ALL Rabbits "+NumberRabbits+"\n"+"Simulation is running for " + currentTime + "ms");
            textArea.setEditable(false);
            textArea.setBackground(Color.BLACK);
            contents.add(textArea);
            setSize(350, 200);
            setLocationRelativeTo(null);
            setVisible(true);
            requestFocus();
        }
    }

    public class currentObject extends JDialog
    {
        public currentObject()
        {
            JButton button=new JButton("OK");
            button.setFocusable(false);
            System.out.println(obj.GetMap().entrySet());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(go) {
                        resume(currentTime);
                    }
                        dispose();
                }
            });
            setTitle("Window");
            TextArea text=new TextArea(5,30);
            String data = "Living objects: "+obj.GetMap().size()+'\n';
            data += "ID\tBirth time\n";
            for (Map.Entry entry : (Set<Map.Entry>)obj.GetMap().entrySet()) {
                data += entry.getValue().toString() + '\t' + entry.getKey() + '\n';
            }
            text.setText(data);
            text.setFont(new Font("Window", Font.BOLD, 14));
            text.setEditable(false);
            JPanel contein=new JPanel();
            contein.add(button);
            contein.add(text);
            setContentPane(contein);
            setSize(350,200);
            setLocationRelativeTo(null);
            setVisible(true);
            requestFocus();
        }
    }

    public class WindowText extends JDialog
    {
        public WindowText() {

            JButton button1 = new JButton("OK");
            button1.setFocusable(false);
            button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            setTitle("Warning");
            TextArea textArea1=new TextArea(5,30);
            textArea1.setText("This line can only contain numbers"+"\n"+" click 'OK' to continue");
            textArea1.setFont(new Font("Window", Font.BOLD, 14));
            textArea1.setEditable(false);
            textArea1.setForeground(Color.BLACK);

            JPanel contents = new JPanel();
            contents.add(button1);
            contents.add(textArea1);
            setContentPane(contents);
            setSize(350, 200);
            setLocationRelativeTo(null);
            frame.requestFocus();
            setVisible(true);

        }
    }

    public void Update(double elapseTime)
    {
        Iterator<Map.Entry> iter = obj.GetMap().entrySet().iterator();
        Vector<Map.Entry> toRemove = new Vector();
        while (iter.hasNext())
        {
            Map.Entry elem = iter.next();//время это key,value это ID
            if (((Integer)elem.getKey()> 0 && (Long)elem.getValue() + lifeTimeRabbit <= currentTime)
                    || ((Integer)elem.getKey() < 0 && (Long)elem.getValue() + lifeTimeAlbino <= currentTime))
                toRemove.add(elem);//Удаление во всех трех коллекциях
        }
        for (Map.Entry elem : toRemove)//has next о
        {
            obj.GetMap().remove(elem.getKey());
            obj.getID().remove((Integer)elem.getKey());
            for (AbstractRabbit rabbit : obj.GetVector()) {
                if (rabbit.ID ==(Integer) elem.getKey()) {
                    obj.GetVector().remove(rabbit);
                    break;
                }
            }
        }
        Random r=new Random();

        if(elapseTime%N1==0 && r.nextInt(1)<P1) {
            concrete.createRabbit(currentTime);
            NumberRabbits++;
            CommonRabbit++;
        }
        if(elapseTime%N2==0 && NumberAlbino<=k*NumberRabbits) {
            concrete.createAlbinoRabbit(currentTime);
            NumberRabbits++;
            NumberAlbino++;
        }
        frame.repaint();
    }

    public int getPanelWith()
    {
        return panel.getWidth();
    }

}