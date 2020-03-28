package Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import LabObjects.ConcreteFactory;
import LabObjects.House;

import static java.awt.event.KeyEvent.*;


public class Habitat extends JFrame {
    private int width = 920;
    private int height = 480;
    private Timer m_timer = new Timer();
    private long t_start = 0;
    private ConcreteFactory factory = new ConcreteFactory();
    private ArrayList<House> container = new ArrayList();
    private JTextArea area1;
    private boolean visible=true;
    private int countWood = 0, countKap = 0;


    private JPanel panel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            House temp;
            for (int i = 0; i < container.size(); i++) {
                temp = container.get(i);
                temp.draw(g);
            }

        }
    };




    public Habitat() {
        super("Город");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));
        CreateGUI();
        pack();
        setVisible(true);
    }


    public void CreateGUI() {
        add(panel);
        panel.setLayout(new BorderLayout());
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case VK_B: {
                        t_start = System.currentTimeMillis();
                        m_timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Update(System.currentTimeMillis()-t_start);
                            }
                        }, 0, 1000);

                        break;
                    }
                    case VK_E: {
                        m_timer.cancel();
                        container.clear();
                        area1 = new JTextArea("Время симуляции:" + (System.currentTimeMillis()-t_start)/1000.d + " секунд"+
                                "\nКоличество деревянных домов: "+countWood +
                                "\nКоличество капитальных домов: "+ countKap, 8, 10);
                        area1.setFont(new Font("TimesRoman", Font.ITALIC, 15));
                        panel.add(area1);
                        repaint();
                        setVisible(true);
                        break;
                    }
                    case VK_T:{
                        if (visible){
                            area1.setVisible(false);
                        visible=false;
                        }
                        else
                        {
                            area1.setVisible(true);
                            visible=true;
                        }
                    }

                }

            }
        });

    }

    public void Update(long time) {
        if (Math.random() < 0.6 && time%2==0) {
            container.add(factory.createWood(height, width));
            countWood++;
        }
        if (Math.random() < 0.3 && time % 1 == 0) {
            container.add(factory.createKap(height, width));
            countKap++;
        }
        repaint();
    }




}




