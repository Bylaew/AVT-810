package Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import LabObjects.ConcreteFactory;
import LabObjects.House;
import jdk.nashorn.internal.scripts.JD;

import static java.awt.event.KeyEvent.*;


public class Habitat extends JFrame {
    private int N1=1;
    private int N2=2;
    private int width = 1080;
    private int height = 720;
    private Timer m_timer;
    private long t_start = 0;
    private ConcreteFactory factory = new ConcreteFactory();
    private ArrayList<House> container = new ArrayList();
    private JTextArea area1;
    private JLabel s_time = new JLabel();
    private JLabel WoodCount = new JLabel();
    private JLabel KapCount = new JLabel();
    private JLabel period_kap_text = new JLabel("Период капитального");
    private JLabel period_wood_text = new JLabel("Период деревянного");
    private JLabel posib_kap_text = new JLabel("Вероятность капитального");
    private JLabel posib_wood_text = new JLabel("Вероятность деревянного");
    private boolean visible=true;
    private int countWood = 0, countKap = 0;
    private JButton start = new JButton("Start");
    private JButton stop = new JButton("Stop");
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
    private double P1=0.1;
    private double P2=0.2;





    private JPanel panel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D info_area = (Graphics2D) g;
            info_area.setColor(new Color(221, 5, 0));
            info_area.fillRect((int)(width*3/4), 0, (int)(width/4), height);
            House temp;
            for (int i = 0; i < container.size(); i++) {
                temp = container.get(i);
                temp.draw(g);
            }

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
            repaint();
            setVisible(true);}
    }

    private void start_sim(){
        m_start.setEnabled(false);
        m_stop.setEnabled(true);
        if(!sim_active){
            container.clear();
            countWood=0;
            countKap=0;
            sim_active = true;
            t_start = System.currentTimeMillis();
            m_timer = new Timer();
            m_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Update(System.currentTimeMillis() - t_start);
                }
            }, 0, 500);
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
        }
        posib_kap.setFocusable(false);
        posib_wood.setFocusable(false);
        posib_wood.setSelectedIndex(1);
        posib_kap.setSelectedIndex(2);
        panel.add(posib_kap_text);
        panel.add(posib_wood_text);
        panel.add(posib_wood);
        panel.add(posib_kap);
        panel.add(period_kap);
        panel.add(period_wood);
        panel.add(period_kap_text);
        panel.add(period_wood_text);
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
        info.setBounds((int)(width*3/4)+10,110,200,15);
        period_kap_text.setBounds((int)(width*3/4)+10,170,200,15);
        period_wood_text.setBounds((int)(width*3/4)+10,190,200,15);
        period_kap.setBounds((int)(width*3/4)+170,170,70,15);
        period_wood.setBounds((int)(width*3/4)+170,190,70,15);
        info.setSelected(false);
        time_visible.setBounds((int)(width*3/4)+10,130,200,15);
        time_invisible.setBounds((int)(width*3/4)+10,150,200,15);
        posib_kap_text.setBounds((int)(width*3/4)+10,210,200,15);
        posib_wood_text.setBounds((int)(width*3/4)+10,235,200,15);
        posib_kap.setBounds((int)(width*3/4)+170, 210,70,20);
        posib_wood.setBounds((int)(width*3/4)+170, 235,70,20);
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
                P1=posib_kap.getSelectedIndex()/10.d;
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
                    visible=false;
                }
                else
                {
                    KapCount.setVisible(true);
                    WoodCount.setVisible(true);
                    if (time_view.getSelection()==time_visible.getModel())
                        s_time.setVisible(true);
                    visible=true;
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
                        visible=false;
                        }
                        else
                        {
                            m_view.setState(true);
                            KapCount.setVisible(true);
                            WoodCount.setVisible(true);
                            if (time_view.getSelection()==time_visible.getModel())
                            s_time.setVisible(true);
                            visible=true;
                        }
                        break;
                    }

                }
                requestFocusInWindow();
            }
        });

    }

    public void Update(long time) {
        if (Math.random() < P1 && time%N1==0) {
            container.add(factory.createWood(work_area_h, work_area_w));
            countWood++;
        }
        if (Math.random() < P2 && time % N2 == 0) {
            container.add(factory.createKap(work_area_h, work_area_w));
            countKap++;
        }
        s_time.setText("Время:" + time/1000.d);
        WoodCount.setText("Деревенные дома:"+countWood);
        KapCount.setText("Капитальные дома:"+countKap);
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
            info.setText("Капитальные дома:"+countKap+"\n Деревянные дома:"+countWood+"\n Время:"+(System.currentTimeMillis() - time_wait - t_start)/1000.d);
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
                            Update(System.currentTimeMillis() - time_wait - t_start);
                        }
                    }, 0, 500);
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




