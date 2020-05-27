package com.company;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Timer;
import javax.swing.*;
import javax.swing.event.*;

///Консоль будет запускать одельный поток в пайпет один поток в мейн а другой в читающий
//Id x и y в 4 пункте передачи
public class Habitat {
    private int NumberRabbits=0;
    private Singleton singleton=Singleton.getInstance();
    JFrame frame;
    private Timer mTimer = new Timer();
    private int NumberAlbino;
    private int CommonRabbit;
     boolean ShowTime=false;
    private boolean simulate=false;
    private boolean JustStart=true;
    public long currentTime;
    public JPanel panel,panelTwo;
     double N1=400;
     double N2=500;
     int lifeTimeRabbit=1000;
     int lifeTimeAlbino=10000;
     float k= (float) 0.1;
     double P1=0.3;
    private long to=0;//используется для resume обновление времни
    private boolean bol=false;
    private boolean go=false;//не дает книпоке currentObj продолжить после остановления симуляции
    private ConcreteFactory concrete;
     JMenuBar menuBar=new JMenuBar();
    private BaceAICommon baceAICommon=null;
    private BaceAIAlbino  baceAIAlbino=null;
    public boolean ready=true;
    public boolean readyAlbino=true;
    public boolean starts=true;
    private ConsoleDialog cd;
    private Accepts acc=new Accepts(this);
    private Socket client;
    private DataInputStream inputStream;
    //private boolean close=false;
    private SQLConnection SQLconnection= SQLConnection.getInstance();
    JComboBox TCPbox=new JComboBox();
    private Connection connection;
    private int Type=-1;
    String strN1,strN2,strLifeRabbit,strLifeAlbino;
    TextField text,textTwo,LifeTextRabbit,LifeTextAlbino;
    Generic<Object>rabbitGeneric=new Generic<>();

    public Habitat(){
        Panel();
        broadcast();
        panelTwo.setLayout(null);
        frame=new JFrame("Window");
        frame.add(panel,BorderLayout.CENTER);
        frame.add(panelTwo,BorderLayout.EAST);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    FileOut();
                    if(connection.running) {
                        connection.CloseConnection();
                        connection.running=false;
                    }
                } catch (IOException ex) {
                    System.out.println("IOException"+ex);
                }
            }
        });
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
                    rabbitGeneric.addCollection(singleton.GetVector());
                    //System.out.println();
                    Stop();
                }
                if(e.getKeyCode()==KeyEvent.VK_T)
                {
                    if(ShowTime) {
                        //System.out.println("11111 "+rabbitGeneric.delete());
                        //singleton.GetVector().remove(rabbitGeneric.delete());
                        //System.out.println("11111 "+singleton.GetVector());
                        rabbitGeneric.mix(singleton.GetVector());/////////////////////////////////////////////
                        ShowTime=false;
                    }
                    else
                    {
                        ShowTime=true;
                    }
                    chekBox();
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
                        synchronized (singleton.GetVector()) {
                            for (int i = 0; i < singleton.GetVector().size(); i++) {
                                try {
                                    g.drawImage(singleton.GetVector().get(i).getImage(), singleton.GetVector().get(i).getX(), singleton.GetVector().get(i).getY(), null);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
        try {
            FileIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void FileOut() throws IOException {
        FileWriter writer=new FileWriter("text1.txt");
        String ShowTimeStr=Boolean.toString(ShowTime);
        String bolStr=Boolean.toString(bol);
        String readyStr=Boolean.toString(ready);
        String readyAlStr=Boolean.toString(readyAlbino);
        String N1Str=Double.toString(N1);
        String N2Str=Double.toString(N2);
        String lifeRabbitStr=Integer.toString(lifeTimeRabbit);
        String lifeAlbinoStr=Integer.toString(lifeTimeAlbino);
        String P1Str=Double.toString(P1);
        String kStr=Float.toString(k);
        writer.write(ShowTimeStr+","+bolStr+","+readyStr+","+readyAlStr+","+N1Str+","+N2Str+","+lifeRabbitStr+","+
                lifeAlbinoStr+","+P1Str+","+kStr);
        writer.close();
    }

    public void FileIn() throws IOException {
        File file=new File("text1.txt");
        FileReader reader=new FileReader(file);
        BufferedReader re=new BufferedReader(reader);
        String line=null;
        while((line=re.readLine())!=null) {
            System.out.println(line);
                splitUp(line);
        }

        re.close();
        reader.close();
    }

    public void splitUp(String line){
        String[] result=line.split(",");
        ShowTime=Boolean.parseBoolean(result[0]);
        bol=Boolean.parseBoolean(result[1]);
        ready=Boolean.parseBoolean(result[2]);
        readyAlbino=Boolean.parseBoolean(result[3]);
        N1=Double.parseDouble(result[4]);
        N2=Double.parseDouble(result[5]);
        lifeTimeRabbit=Integer.parseInt(result[6]);
        lifeTimeAlbino=Integer.parseInt(result[7]);
        P1=Double.parseDouble(result[8]);
        k=Float.parseFloat(result[9]);
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
                synchronized (baceAICommon)
                {
                    ready=true;
                    baceAICommon.notify();
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
                synchronized (baceAIAlbino)
                {
                    readyAlbino=true;
                    baceAIAlbino.notify();
                }
            }
        });
        panelTwo.add(buttonNoti);
        buttonNoti.setBounds(5,460,100,25);
    }

    public void chekBox() {
        ButtonGroup currentT=new ButtonGroup();
        JRadioButton on=new JRadioButton("Visible time");
        JRadioButton off=new JRadioButton("Not Visible time",true);
        on.setFocusable(false);
        off.setFocusable(false);
        currentT.add(on);
        currentT.add(off);
        //cbProbability.setSelectedIndex(3);
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
        //on.setSelected(ShowTime);
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
                }else {
                    bol = true;
                }
            }
        });
        if(bol)
        {
            boxInformation.setSelected(true);
        }else
            boxInformation.setSelected(false);
        boxInformation.setBounds(105,40,90,30);
        boxInformation.setFocusable(false);
        panelTwo.add(boxInformation);
        panelTwo.setPreferredSize(new Dimension(200, 100));
    }
    JList Present;

    public void listertr(){
        Present= new JList();
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
        Present.setSelectedIndex((int) (k*10));
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
        Present.setRequestFocusEnabled(false);
        labelPercent.setBounds(5,110,70,25);
        Present.setBounds(5,130,35,200);
        panelTwo.add(labelPercent);
        panelTwo.add(Present);
    }

    JComboBox cbProbability;
    public void KomboBox()
    {
        cbProbability=new JComboBox();
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
        cbProbability.setSelectedIndex((int)(P1*10));
        cbProbability.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                JComboBox bx=(JComboBox)itemEvent.getSource();
                P1= (double) bx.getSelectedIndex()/10;
                System.out.println(P1);
            }
        });
        cbProbability.setFocusable(false);
       // cbProbability.setRequestFocusEnabled(false);
        JLabel labelCb=new JLabel("Probability rabbit");
        labelCb.setBounds(70,110,100,20);
        cbProbability.setBounds(70,130,80,30);
        panelTwo.add(labelCb);
        panelTwo.add(cbProbability);

        JComboBox BOXrabbit=new JComboBox();
        BOXrabbit.addItem("MAX");
        BOXrabbit.addItem("MIN");
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
        BOXalbino.addItem("MAX");
        BOXalbino.addItem("MIN");
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
        strN1=Double.toString(N1);
        text=new TextField(strN1);
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    try {
                        N1=Integer.parseInt(text.getText());
                       // System.out.println(N1);
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

        strN2=Double.toString(N2);
        textTwo=new TextField(strN2);
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

        strLifeRabbit=Integer.toString(lifeTimeRabbit);
        LifeTextRabbit = new TextField();
        LifeTextRabbit.setText(strLifeRabbit);
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

        strLifeAlbino=Integer.toString(lifeTimeAlbino);
        LifeTextAlbino=new TextField(strLifeAlbino);
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
        JMenuItem save =new JMenuItem("Save");
        JMenuItem loading =new JMenuItem("loading");
        ButtonGroup onAndOff=new ButtonGroup();
        JRadioButtonMenuItem one=new JRadioButtonMenuItem("on");
        JRadioButtonMenuItem two=new JRadioButtonMenuItem("off");
        JMenuItem ConsoleMenu = new JMenuItem("Console");
        Menu.add(StarMenu);
        Menu.add(StopMenu);
        Menu.add(save);
        Menu.add(loading);
        onAndOff.add(one);
        onAndOff.add(two);
        Menu.add(one);
        Menu.add(two);
        Menu.add(ConsoleMenu);

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
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    objSave();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loading.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    ObjLoading();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
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

        ConsoleMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                cd = new ConsoleDialog();
                acc.setPr(cd.getStream());
                cd.setPr(acc.getStream());
                new Thread(cd).start();
                new Thread(acc).start();
            }
        });
        Menu.setFocusable(false);
        menuBar.setFocusable(false);
        menuBar.add(Menu);

        JMenu menuSQL=new JMenu("SQL");
        JMenuItem Keep=new JMenuItem("Save");
        JMenuItem load=new JMenuItem("loading");
        JRadioButtonMenuItem ordinary= new JRadioButtonMenuItem("ordinary rabbits");
        JRadioButtonMenuItem albino =new JRadioButtonMenuItem("albino rabbits");
        JRadioButtonMenuItem ollRabbits=new JRadioButtonMenuItem("oll rabbits",true);
        ButtonGroup rabbitsGroup=new ButtonGroup();
        menuSQL.add(Keep);
        menuSQL.add(load);
        rabbitsGroup.add(ollRabbits);
        rabbitsGroup.add(ordinary);
        rabbitsGroup.add(albino);
        menuSQL.add(ordinary);
        menuSQL.add(albino);
        menuSQL.add(ollRabbits);

        ollRabbits.setFocusable(false);
        ordinary.setFocusable(false);
        albino.setFocusable(false);
        Keep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
           SQLconnection.setData(Type);// 0 обычные кролики, 1 альбиносы,-1 все
           System.out.println(singleton.GetVector());
            }
        });
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NumberRabbits=0;
                currentTime=0;
                CommonRabbit=0;
                NumberAlbino=0;
                starts=false;
                singleton.GetVector().clear();
                singleton.GetMap().clear();
                singleton.getID().clear();
                SQLconnection.Data(Type);
            }
        });
        ollRabbits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Type=-1;
            }
        });
        ordinary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Type=0;
            }
        });
        albino.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Type=1;
            }
        });
        menuSQL.setFocusable(false);
        menuBar.add(menuSQL);
    }

    public void objSave() throws IOException {
        FileOutputStream file=new FileOutputStream("myObj.ser");
        ObjectOutputStream os = new ObjectOutputStream(file);

        for(AbstractRabbit rabbit:singleton.GetVector()) {
            os.writeObject(rabbit);
            System.out.println(rabbit);
        }
        os.flush();
        os.close();
    }

    public void ObjLoading() throws IOException, ClassNotFoundException {
        JFileChooser fc = new JFileChooser();
        simulate=false;
        mTimer.cancel();
        fc.setCurrentDirectory(new File("myObj.ser"));
        fc.showOpenDialog(frame);
        File selFile = fc.getSelectedFile();
        FileInputStream file=new FileInputStream(selFile);
        ObjectInputStream os=new ObjectInputStream(file);

        NumberRabbits=0;
        currentTime=0;
        CommonRabbit=0;
        NumberAlbino=0;
        starts=false;
        currentTime=0;
        singleton.GetVector().clear();
        singleton.GetMap().clear();
        singleton.getID().clear();
        while (true) {
            try {
                AbstractRabbit rabbit=(AbstractRabbit) os.readObject();
                rabbit.BirthTime=0;
                System.out.println(rabbit);
                singleton.GetVector().add(rabbit);
                singleton.getID().add(rabbit.getID());
                singleton.GetMap().put(rabbit.getID(),rabbit.BirthTime);
            }catch (IOException e)
            {
                os.close();
                System.out.println("exit");
                break;
            }
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
        System.out.println(ready);
        System.out.println(readyAlbino);
        concrete= new ConcreteFactory();
        new Thread(baceAICommon).start();
        new Thread(baceAIAlbino).start();
        if (JustStart)
            JustStart = false;
        mTimer.cancel();
        if(starts) {
            singleton.refreshMap();
            singleton.refreshID();
            singleton.refreshVector();
        }
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
        starts=true;
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
            System.out.println(singleton.GetMap().entrySet());
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
            String data = "Living objects: "+singleton.GetMap().size()+'\n';
            data += "ID\tBirth time\n";
            for (Map.Entry entry : (Set<Map.Entry>)singleton.GetMap().entrySet()) {
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
        Iterator<Map.Entry> iter = singleton.GetMap().entrySet().iterator();
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
            singleton.GetMap().remove(elem.getKey());
            singleton.getID().remove((Integer)elem.getKey());
            for (AbstractRabbit rabbit : singleton.GetVector()) {
                if (rabbit.ID ==(Integer) elem.getKey()) {
                    singleton.GetVector().remove(rabbit);
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

    public String Execute(String command)
    {
        if (command.equals("clear")) {
            return "";
        }
        String[] parts = command.split(" ");
        if (parts.length == 3 && parts[0].equals("reduce") && parts[1].equals("albino"))
        {
            try {
                int reduce = Integer.parseInt(parts[2]);
                ReduceAlbino(reduce);
            }
            catch (NumberFormatException e)
            {
                return "Incorrect parameter for reduce albino command";
            }
            return "Albino number is reduced!";
        }
        if(parts.length==2 && parts[0].equals("get")&&parts[1].equals("ID")){/////////////////////////////////
                return "ID " + singleton.GetMap().keySet();
        }
        if(parts.length==5 && parts[0].equals("add")&& parts[1].equals("rabbit")&& parts[2].equals("to")&& parts[3].equals("list"))
        {
            int num =Integer.parseInt(parts[4]);
            boolean peremen =false;
            //singleton.GetVector().get(reduce);
            //System.out.println("vector "+ singleton.GetVector());
            //for(AbstractRabbit rab:singleton.GetVector())
                for(AbstractRabbit rabbit: singleton.GetVector()) {
                    if(num==rabbit.getID()) {
                        System.out.println("____ " + rabbit);
                        rabbitGeneric.add(rabbit);
                        peremen=true;
                    }
                }
                if(peremen)
                    return "rabbit added";
                else
                    return "wrong id or rabbit no longer";
        }
        if(parts.length==2 && parts[0].equals("remove") && parts[1].equals("rabbits"))//удаление последнего элемнта в списке
        {
            System.out.println(rabbitGeneric.delete());
            return "remove last Element";
        }
        if(parts.length==4 && parts[0].equals("add")&& parts[1].equals("collection")&& parts[2].equals("to")&& parts[3].equals("list"))
        {
            rabbitGeneric.addCollection(singleton.GetVector());
            return "Collection added";
        }
        if(parts.length==2 && parts[0].equals("show")&&parts[1].equals("list"))
        {
            return "Sheet displayed"+rabbitGeneric.show().toString();
        }
        if(parts.length==2 && parts[0].equals("shuffle")&&parts[1].equals("rabbits"))
        {
            return "shuffle rabbits performed"+rabbitGeneric.mix(rabbitGeneric.show());
        }
        if(parts.length==2 && parts[0].equals("remove")&& parts[1].equals("all"))
        {
            rabbitGeneric.removeAll();
            return "list is cleared";
        }
        return "Unknown command";
    }

    public int getCurrentAlbinoNumber()//нужен для различия альбиноса от обычного кролика
    {
        int result = 0;
        for (AbstractRabbit rabbit: singleton.GetVector())
        {
            if (rabbit.getID() < 0)
                result++;
        }
        return result;
    }

    public void ReduceAlbino (int number) {
        int i = getCurrentAlbinoNumber()*number/100;
        System.out.println(i);
        int deleted = 0;
        Iterator<AbstractRabbit> iter = singleton.GetVector().iterator();
        while (iter.hasNext())
        {
            if (deleted < i)
            {
                AbstractRabbit rabbit = iter.next();
                if (rabbit.getID() < 0)
                {
                    singleton.GetMap().remove(rabbit.getID());
                    singleton.getID().remove(rabbit.getID());
                    iter.remove();
                    singleton.GetVector();
                    singleton.getID();
                    deleted++;
                }
            }
            else
                break;
        }
    }

    public void broadcast(){
        TCPbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(TCPbox.getSelectedIndex()>0){
                    connection.broadcastTread();
                    System.out.println(N1);
                }
            }
        });
                TCPbox.setFocusable(false);
                TCPbox.setFocusTraversalPolicyProvider(false);
                JLabel TCPlable = new JLabel("Clients");
                TCPbox.setBounds(5, 500, 120, 25);
                TCPlable.setBounds(10, 480, 80, 25);
                panelTwo.add(TCPlable);
                panelTwo.add(TCPbox);

                connection=new Connection(this);
                new Thread(connection).start();
                System.out.println(N1);
                //Texti();
    }

}