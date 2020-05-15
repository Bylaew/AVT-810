package Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

import LabObjects.ConcreteFactory;
import LabObjects.House;
import LabObjects.KapAI;
import LabObjects.WoodAI;

import javax.sound.sampled.*;

import static java.awt.event.KeyEvent.*;


public class Habitat extends JFrame {
    private int N1=1;
    private int N2=2;
    private int width = 1080;
    private int height = 720;
    private Timer m_timer;
    private long t_start = 0;
    private ConcreteFactory factory = new ConcreteFactory();
    private JLabel s_time = new JLabel();
    private JLabel WoodCount = new JLabel();
    private JLabel KapCount = new JLabel();
    private JLabel period_kap_text = new JLabel("Период капитального");
    private JLabel period_wood_text = new JLabel("Период деревянного");
    private JLabel posib_kap_text = new JLabel("Вероятность капитального");
    private JLabel posib_wood_text = new JLabel("Вероятность деревянного");
    private JLabel edit_life_wood_text = new JLabel("Время жизни деревянного");
    private JLabel edit_life_kap_text = new JLabel("Время жизни капитального");
    private JLabel woodAI_lab = new JLabel("Интеллект дерева");
    private JLabel kapAI_lab = new JLabel("Интеллект капитала");
    private JButton start = new JButton("Start");
    private JButton stop = new JButton("Stop");
    private JButton curr_obj = new JButton("Текущие объекты");
    private JButton start_woodAI = new JButton("Запуск");
    private JButton start_kapAI = new JButton("Запуск");
    private JButton stop_woodAI = new JButton("Остановка");
    private JButton stop_kapAI = new JButton("Остановка");
    private boolean sim_active = false;
    private int work_area_w=(width*3/4);
    private int work_area_h=height*3/4;
    private JCheckBox info = new JCheckBox("Показывать информацию");
    private ButtonGroup time_view= new ButtonGroup();
    private JRadioButton time_visible = new JRadioButton("Показывать время");
    private JRadioButton time_invisible = new JRadioButton("Скрыть время");
    private JMenuBar mainmenu = new JMenuBar();
    private JMenu sim = new JMenu("Симуляция");
    private JMenu infor_menu = new JMenu("Вид");
    private JMenuItem m_start = new JMenuItem("Начать эксперимент");
    private JMenuItem m_stop = new JMenuItem("Завершить эксперимент");
    private JCheckBoxMenuItem m_view = new JCheckBoxMenuItem("Показать статистику");
    private long time_stop = 0, time_wait;
    private boolean info_view=false;
    private JTextField period_wood = new JTextField();
    private JTextField period_kap = new JTextField();
    private JComboBox posib_wood = new JComboBox();
    private JComboBox posib_kap = new JComboBox();
    private JComboBox wood_priority = new JComboBox();
    private JComboBox kap_proirity = new JComboBox();
    private double P1=0.1;
    private double P2=0.2;
    private int life_time_wood = 2;
    private int life_time_kap = 5;
    private boolean woodAI_work=true;
    private boolean kapAI_work=true;
    private JTextField edit_life_wood = new JTextField();
    private JTextField edit_life_kap = new JTextField();
    private KapAI intel_kap = new KapAI(this);
    private WoodAI intel_wood = new WoodAI(this);






    private JPanel panel = new JPanel() {
        @Override
        public synchronized void paintComponent(Graphics g) {
            super.paintComponent(g);
            House temp;
            for (int i = 0; i < Collect.getInstance().size_obj(); i++) {
                g.drawImage(Collect.getInstance().get_obj(i).getImage(), Collect.getInstance().get_obj(i).getX(), Collect.getInstance().get_obj(i).getY(),null);
            }
            Graphics2D info_area = (Graphics2D) g;
            info_area.setColor(new Color(221, 5, 0));
            info_area.fillRect((int)(width*3/4), 0, (int)(width/4), height);
            g.drawOval(getWidth()*3/8, getHeight()/2, 10,10);
            g.setColor(Color.RED);

        }
    };

    private void stop_sim(){
        time_stop = System.currentTimeMillis();
        if(sim_active){
            m_stop.setEnabled(false);
            m_start.setEnabled(true);
            m_timer.cancel();
            sim_active=false;
            if(info_view){
            MyDialog msgbox = new MyDialog(Habitat.this, "Информация", true );
            msgbox.setVisible(true);
            }
            else{
                m_timer.cancel();
            }
            setVisible(true);}
    }

   private void start_sim(){
            m_start.setEnabled(false);
        m_stop.setEnabled(true);
        if(!sim_active){
            Collect.getInstance().clear();
            sim_active = true;
            t_start = System.currentTimeMillis();
            m_timer = new Timer();
            m_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Update((System.currentTimeMillis() - t_start)/1000);
                }
            }, 0, 1000);
        }
    }





    public Habitat() {
        super("Коммунизм");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));
        CreateGUI();
        setVisible(true);
    }


    public void CreateGUI() {
        time_view.add(time_visible);
        time_view.add(time_invisible);
        add(panel);
        for (int i=0; i<11;i++){
            posib_kap.addItem(i/10.d);
            posib_wood.addItem(i/10.d);
            if(i!=0) {
                wood_priority.addItem(i);
                kap_proirity.addItem(i);
            }
        }
        posib_kap.setFocusable(false);
        posib_wood.setFocusable(false);
        posib_wood.setSelectedIndex(1);
        posib_kap.setSelectedIndex(2);
        wood_priority.setSelectedIndex(4);
        kap_proirity.setSelectedIndex(4);
        panel.add(posib_kap_text);
        panel.add(posib_wood_text);
        panel.add(posib_wood);
        panel.add(posib_kap);
        panel.add(period_kap);
        panel.add(period_wood);
        panel.add(period_kap_text);
        panel.add(period_wood_text);
        panel.add(edit_life_kap);
        panel.add(edit_life_kap_text);
        panel.add(edit_life_wood);
        panel.add(edit_life_wood_text);
        panel.add(curr_obj);
        panel.add(start_woodAI);
        panel.add(start_kapAI);
        panel.add(stop_woodAI);
        panel.add(stop_kapAI);
        panel.add(woodAI_lab);
        panel.add(kapAI_lab);
        panel.add(wood_priority);
        panel.add(kap_proirity);
        ImageIcon icon = new ImageIcon("./resources/USSR.png");
        setIconImage(icon.getImage());
        sim.add(m_start);
        sim.add(m_stop);
        infor_menu.add(m_view);
        mainmenu.add(sim);
        mainmenu.add(infor_menu);
        setJMenuBar(mainmenu);
       KapCount.setBounds((int)(width*3/4)+10, 40, 300, 15);
        WoodCount.setBounds((int)(width*3/4)+10, 25, 300, 15);
        s_time.setBounds((int)(width*3/4)+10, 10, 100, 15);
        start.setBounds((int)(width*3/4)+10,70,200,15);
        stop.setBounds((int)(width*3/4)+10,90,200,15);
        curr_obj.setBounds((int)(width*3/4)+10,110,200,15);
        info.setBounds((int)(width*3/4)+10,130,200,15);
        period_kap_text.setBounds((int)(width*3/4)+10,190,200,15);
        period_wood_text.setBounds((int)(width*3/4)+10,210,200,15);
        period_kap.setBounds((int)(width*3/4)+170,190,70,15);
        period_wood.setBounds((int)(width*3/4)+170,210,70,15);
        info.setSelected(false);
        time_visible.setBounds((int)(width*3/4)+10,150,200,15);
        time_invisible.setBounds((int)(width*3/4)+10,170,200,15);
        posib_kap_text.setBounds((int)(width*3/4)+10,230,200,15);
        posib_wood_text.setBounds((int)(width*3/4)+10,255,200,15);
        posib_kap.setBounds((int)(width*3/4)+170, 230,70,20);
        posib_wood.setBounds((int)(width*3/4)+170, 255,70,20);
        edit_life_kap_text.setBounds((int)(width*3/4)+10,280,200,15);
        edit_life_wood_text.setBounds((int)(width*3/4)+10,305,200,15);
        edit_life_kap.setBounds((int)(width*3/4)+170, 280,70,20);
        edit_life_wood.setBounds((int)(width*3/4)+170, 305,70,20);
        woodAI_lab.setBounds((int)(width*3/4)+10, 330,120,20);
        kapAI_lab.setBounds((int)(width*3/4)+130, 330,120,20);
        start_woodAI.setBounds((int)(width*3/4)+10, 360,100,20);
        start_kapAI.setBounds((int)(width*3/4)+130, 360,100,20);
        stop_woodAI.setBounds((int)(width*3/4)+10, 390,100,20);
        stop_kapAI.setBounds((int)(width*3/4)+130, 390,100,20);
        wood_priority.setBounds((int)(width*3/4)+10, 420,100,20);
        kap_proirity.setBounds((int)(width*3/4)+130, 420,100,20);
        panel.add(s_time);
        panel.add(WoodCount);
        panel.add(KapCount);
        panel.add(start);
        panel.add(stop);
        panel.add(info);
        panel.add(time_invisible);
        panel.add(time_visible);
        panel.setLayout(new BorderLayout());
        start.setFocusable(false);
        stop.setFocusable(false);
        info.setFocusable(false);
        time_visible.setFocusable(false);
        time_invisible.setFocusable(false);
        m_stop.setEnabled(false);
        m_view.setState(true);
        time_view.setSelected(time_visible.getModel(), true);
        pack();
        setFocusable(true);
        intel_kap.start();
        intel_wood.start();


        stop_woodAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (woodAI_work){
                    intel_wood.stop();
                    woodAI_work=false;
                    requestFocusInWindow();
                }
            }
        });


        stop_kapAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (kapAI_work){
                    intel_kap.stop();
                    kapAI_work=false;
                    requestFocusInWindow();
                }
            }
        });

        start_woodAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!woodAI_work){
                    intel_wood.resume();
                    woodAI_work=true;
                    requestFocusInWindow();
                }
            }
        });

        start_kapAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!kapAI_work){
                    intel_kap.resume();
                    kapAI_work=true;
                    requestFocusInWindow();
                }
            }
        });


        m_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start_sim();
                requestFocusInWindow();
            }
        });



        info.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!info_view)
                info_view = true;
                else
                    info_view=false;
                requestFocusInWindow();
            }
        });

        curr_obj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dialog_objects msgbox3 = new Dialog_objects(Habitat.this, "Живые", true);
                msgbox3.setVisible(true);
            }
        });

        period_wood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String field1 = new String();
                field1 = period_wood.getText();
                try{
                    N1=Integer.parseInt(field1);
                }catch(NumberFormatException nfe){
                MyError error = new MyError(Habitat.this, "Ошибка", true);
error.setVisible(true);
N1 = 1;
                }
                requestFocusInWindow();
            }
        });



        period_kap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String field1 = new String();
                field1 = period_kap.getText();
                try{
                    N2=Integer.parseInt(field1);
                }catch(NumberFormatException nfe){
                    MyError error = new MyError(Habitat.this, "Ошибка", true);
                    error.setVisible(true);
                    N2 = 2;
                }
                requestFocusInWindow();
            }
        });

        edit_life_wood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String field1 = new String();
                field1 = edit_life_wood.getText();
                try{
                    life_time_wood=Integer.parseInt(field1);
                }catch(NumberFormatException nfe){
                    MyError error = new MyError(Habitat.this, "Ошибка", true);
                    error.setVisible(true);
                    life_time_wood = 2;
                }
                requestFocusInWindow();
            }
        });

        edit_life_kap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String field1 = new String();
                field1 = edit_life_kap.getText();
                try{
                    life_time_kap=Integer.parseInt(field1);
                }catch(NumberFormatException nfe){
                    MyError error = new MyError(Habitat.this, "Ошибка", true);
                    error.setVisible(true);
                    life_time_kap = 5;
                }
                requestFocusInWindow();
            }
        });



        posib_kap.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                P2=posib_kap.getSelectedIndex()/10.d;
                requestFocusInWindow();
            }
        });

        posib_wood.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                P1=posib_wood.getSelectedIndex()/10.d;
                requestFocusInWindow();
            }
        });

        wood_priority.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                intel_wood.setPriority(wood_priority.getSelectedIndex()+1);
                requestFocusInWindow();
            }
        });

        kap_proirity.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                intel_kap.setPriority(kap_proirity.getSelectedIndex()+1);
                requestFocusInWindow();
            }
        });

        m_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             stop_sim();
                requestFocusInWindow();
            }
        });

        m_view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!m_view.getState()){
                    KapCount.setVisible(false);
                    WoodCount.setVisible(false);
                    if (time_view.getSelection()==time_invisible.getModel())
                        s_time.setVisible(false);
                }
                else
                {
                    KapCount.setVisible(true);
                    WoodCount.setVisible(true);
                    if (time_view.getSelection()==time_visible.getModel())
                        s_time.setVisible(true);
                }
                requestFocusInWindow();
            }
        });

        time_invisible.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                s_time.setVisible(false);
                requestFocusInWindow();
            }
        });

        time_visible.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                s_time.setVisible(true);
                requestFocusInWindow();
            }
        });



        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            start_sim();
                requestFocusInWindow();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                stop_sim();
                requestFocusInWindow();
            }
        });







        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case VK_B: {
                        start_sim();
                        break;
                    }
                    case VK_E: {
                        stop_sim();
                        break;
                    }
                    case VK_T:{
                        if (m_view.getState()){
                            m_view.setState(false);
                            KapCount.setVisible(false);
                            WoodCount.setVisible(false);
                            if (time_view.getSelection()==time_invisible.getModel())
                            s_time.setVisible(false);
                        }
                        else
                        {
                            m_view.setState(true);
                            KapCount.setVisible(true);
                            WoodCount.setVisible(true);
                            if (time_view.getSelection()==time_visible.getModel())
                            s_time.setVisible(true);
                        }
                        break;
                    }

                }
                requestFocusInWindow();
            }
        });

    }

    public synchronized void Update(long time) {
        int temp_ID;
        if (Math.random() < P1 && time%N1==0) {
            do{
                temp_ID = (int)(Math.random()*10000);
            }while(!Collect.getInstance().add_ID(temp_ID));
            Collect.getInstance().add_obj(factory.createWood(work_area_h, work_area_w,temp_ID),time, this);
        }
        if (Math.random() < P2 && time % N2 == 0) {
            do{
                temp_ID = (int)(Math.random()*10000);
            }while(!Collect.getInstance().add_ID(temp_ID));
            Collect.getInstance().add_obj(factory.createKap(work_area_h, work_area_w,temp_ID), time, this);
        }
        Collect.getInstance().round(time, life_time_wood, life_time_kap);
        s_time.setText("Время:" + time);
        WoodCount.setText("Деревенные дома:"+Collect.getInstance().Wood_count());
        KapCount.setText("Капитальные дома:"+Collect.getInstance().Kap_count());
        repaint();
    }

    class MyDialog extends JDialog {
        protected JButton ok = new JButton("Ок");
        protected JButton stop = new JButton("Отмена");
        private JPanel buttons = new JPanel();
        private JPanel text = new JPanel();
        private int width = 500, height = 300;

        public MyDialog (JFrame parent, String title, boolean modal){
            super(parent, title, modal);
            setSize(width,height);
            add(buttons);
            add(text);
            text.setBounds(0,0,width,3*height/4);
            buttons.setBounds(0,3*height/4,width,height/4);
            JTextArea info = new JTextArea(3,20);
            info.setEditable(false);
            buttons.add(ok);
            buttons.add(stop);
            text.add(info, BorderLayout.CENTER);
            info.setText("Капитальные дома:"+Collect.getInstance().Kap_count()+"\n Деревянные дома:"+Collect.getInstance().Wood_count()+"\n Время:"+(System.currentTimeMillis() - time_wait - t_start)/1000.d);
            info.setVisible(true);
            info.setSize(100,100);
            stop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    sim_active=true;
                    m_timer = new Timer();
                    time_wait = System.currentTimeMillis()-time_stop;
                    m_timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Update((System.currentTimeMillis() - time_wait - t_start)/1000);
                        }
                    }, 0, 1000);
                }
            });
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    m_timer.cancel();
                    setVisible(false);
                    dispose();
                }
            });



        }
    }

    class Dialog_objects extends JDialog{
        private JButton ok = new JButton("Ок");
        private JPanel buttons = new JPanel();
        private JPanel text = new JPanel();
        private JTextArea live_objects = new JTextArea(10, 30);
        private JScrollPane live_panel= new JScrollPane(live_objects);
        String str = new String();
        private int width = 400, height = 300;
        public Dialog_objects(JFrame parent, String title, boolean modal){
            super(parent, title, modal);
            setSize(width, height);
            add(buttons);
            add(text);
            text.add(live_panel, BorderLayout.CENTER);
            text.setBounds(0,0,width,3*height/4);
            buttons.setBounds(0,3*height/4,width,height/4);
            buttons.add(ok);
            live_panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            live_objects.setEditable(false);
            Collection live = (Collect.getInstance().comeback()).entrySet();
            Iterator it = live.iterator();
            while(it.hasNext()){
                Map.Entry me = (Map.Entry)it.next();
                str = str+"ID объекта: "+me.getKey()+ "  Время рождения: "+me.getValue()+'\n';
            }
            live_objects.setText(str);
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            });
        }


    }



    class MyError extends JDialog {
        protected JButton ok = new JButton("Ок");
        private JPanel buttons = new JPanel();
        private JPanel text = new JPanel();
        private int width = 400, height = 300;
        public MyError (JFrame parent, String title, boolean modal){
            super(parent, title, modal);
            setSize(width,height);
            add(buttons);
            add(text);
            text.setBounds(0,0,width,3*height/4);
            buttons.setBounds(0,3*height/4,width,height/4);
            JTextArea info = new JTextArea(3,20);
            info.setEditable(false);
            buttons.add(ok, BorderLayout.CENTER);
            text.add(info, BorderLayout.CENTER);
            info.setText("Вы ввели некорректные данные");
            info.setVisible(true);
            info.setSize(200,200);
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            });



        }
    }
}




