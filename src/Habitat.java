import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Habitat {
    Singleton singleton = Singleton.getInstance();
    SideMenu sideMenu;
    MenuBar mainMenu;
    JFrame frame;
    ImagePanel window;
    private int width;
    private int height;
    private Timer startTime;
    private long timeFromStart = 0;
    private float N1, N2, P1, P2;
    private ConcreteFactory concreteFactory;
    private boolean isRunning = false;
    private boolean isStatsAllowed;
    private boolean isTimeAllowed;
    private JLabel labelTime;
    private long carLifetime = 15, motoLifetime = 5;

    public void init(float N1, float N2, float P1, float P2) {
        concreteFactory = new ConcreteFactory();
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.P2 = P2;

        frame = new JFrame("Lab 3");

        window = new ImagePanel();
        frame.add(window, BorderLayout.CENTER);

        sideMenu = new SideMenu();
        frame.add(sideMenu, BorderLayout.EAST);

        mainMenu = new MenuBar();
        frame.setJMenuBar(mainMenu);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double w = screenSize.getWidth();
        double h = screenSize.getHeight();
        frame.setPreferredSize(new Dimension((int)(w/1.5), (int)(h/1.5)));


        labelTime = new JLabel();
        labelTime.setText("0:00");
        labelTime.setVisible(false);
        labelTime.setFont(new Font("Consolas", Font.PLAIN, 45));
        window.add(labelTime);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setResizable(true);
        frame.pack();

        startTime = new Timer(1000, e -> {
            timeFromStart++;
            labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
            update(timeFromStart, window);
            sideMenu.timeText.setText("Время: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60 + " секунд(ы)");
        });


        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    resume();
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    pause();

                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    if (labelTime.isVisible()) {
                        labelTime.setVisible(false);
                        isTimeAllowed = false;
                    } else {
                        labelTime.setVisible(true);
                        isTimeAllowed = true;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    if (isRunning) {
                        pause();
                    } else {
                        resume();
                    }
                    sideMenu.UpdateButtons();
                }
                sideMenu.UpdateButtons();
                mainMenu.UpdateButtons();
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                width = window.getWidth();
                height = window.getHeight();
            }
        });

        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                window.requestFocus();
            }
        });
        frame.setVisible(true);
        window.requestFocus();
        width = window.getWidth();
        height = window.getHeight();
    }

    private void pause() {
        startTime.stop();
        isRunning = false;
        sideMenu.UpdateButtons();
        mainMenu.UpdateButtons();
        if (isStatsAllowed) {
            new stats();
        }
    }

    private void resume() {
        startTime.start();
        isRunning = true;
        sideMenu.UpdateButtons();
        mainMenu.UpdateButtons();
    }


    private void update(float time, ImagePanel window) {
        if (time % N1 == 0) {
            if (Math.random() < P1) {
                int id = singleton.generateId();
                singleton.vehicles.add(concreteFactory.createCar((float) (Math.random() * (width - 200)), (float) Math.random() * (height - 200) + 50, id));
                singleton.idSet.add(id);
                singleton.vehicles.get(singleton.vehicles.size() - 1).setBirthTime(timeFromStart);
                singleton.vehicles.get(singleton.vehicles.size() - 1).setLifeTime(carLifetime);
                singleton.Map.put(singleton.vehicles.get(singleton.vehicles.size() - 1).getId(), singleton.vehicles.get(singleton.vehicles.size() - 1).getBirthTime());
            }
        }
        if (time % N2 == 0) {
            if (Math.random() < P2) {
                int id = singleton.generateId();
                singleton.vehicles.add(concreteFactory.createMoto((float) Math.random() * (width - 200), (float) Math.random() * (height - 200) + 50, id));
                singleton.idSet.add(id);
                singleton.vehicles.get(singleton.vehicles.size() - 1).setBirthTime(timeFromStart);
                singleton.vehicles.get(singleton.vehicles.size() - 1).setLifeTime(motoLifetime);
                singleton.Map.put(singleton.vehicles.get(singleton.vehicles.size() - 1).getId(), singleton.vehicles.get(singleton.vehicles.size() - 1).getBirthTime());
            }
        }

        for (int i = 0; i < singleton.vehicles.size(); i++) {
            if (singleton.vehicles.get(i).getLifeTime() + singleton.vehicles.get(i).getBirthTime() <= timeFromStart) {
                singleton.Map.remove(singleton.vehicles.get(i).getId());
                singleton.idSet.remove(singleton.vehicles.get(i).getId());
                singleton.vehicles.remove(i);

            }
        }


        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics offscreenGraphics = buffer.getGraphics();
        offscreenGraphics.setColor(window.getBackground());
        offscreenGraphics.fillRect(0, 0, width, height);

        for (int i = 0; i < singleton.vehicles.size(); i++) {
            offscreenGraphics.drawImage(singleton.vehicles.get(i).getImage(), (int) singleton.vehicles.get(i).getX(), (int) singleton.vehicles.get(i).getY(), singleton.vehicles.get(i).getImage().getWidth(null), singleton.vehicles.get(i).getImage().getHeight(null), null);
        }

        window.setImage(buffer);
        window.repaint();
    }


    class SideMenu extends JPanel {
        JButton start = new JButton("Старт");
        JButton stop = new JButton("Стоп");
        JCheckBox show_info;
        JRadioButton show, hide;
        JTextArea timeText;


        SideMenu() {
            setLayout(new GridLayout(16, 1));
            show_info = new JCheckBox("Показывать статистику");
            show_info.setFocusable(false);
            stop.setEnabled(false);
            start.setFocusable(false);
            stop.setFocusable(false);
            ButtonGroup timeGroup = new ButtonGroup();
            show = new JRadioButton("Показывать время симуляции", true);
            timeGroup.add(show);
            show.setFocusable(false);
            hide = new JRadioButton("Скрывать время симуляции");
            timeGroup.add(hide);
            hide.setFocusable(false);
            hide.setSelected(true);
            timeText = new JTextArea();
            timeText.setFont(new Font("TimesRoman", Font.ITALIC, 14));
            timeText.setText("Время: " + (timeFromStart) + " секунд(ы)");
            timeText.setBackground(getBackground());
            timeText.setEditable(false);
            timeText.setFocusable(false);
            JLabel txt1 = new JLabel("Период появления автомобилей ");
            txt1.setFocusable(false);
            JLabel txt2 = new JLabel("Период появления мотоциклов");
            txt2.setFocusable(false);
            JLabel txt3 = new JLabel("Вероятность появления атомобилкей");
            txt3.setFocusable(false);
            TextField auto_period = new TextField("", 10);
            auto_period.setEnabled(true);
            TextField moto_period = new TextField("", 10);
            moto_period.setEnabled(true);
            var comboBox = new JComboBox();
            for (int i = 10; i <= 100; i += 10)
                comboBox.addItem((float) i / 100.0f);
            comboBox.setSelectedItem(P1);

            JLabel txt4 = new JLabel("Время жизни автомобилей");
            JSlider slider = new JSlider(0, 50, (int)carLifetime);
            slider.setMajorTickSpacing(10);
            slider.setMajorTickSpacing(5);
            slider.setPaintLabels(true);
            slider.setPaintTicks(true);

            JLabel txt5 = new JLabel("Время жизни мотоциклов");
            TextField motolifefield = new TextField("", 10);
            motolifefield.setText(""+motoLifetime);



            add(start);
            add(stop);
            add(show_info);
            add(show);
            add(hide);
            add(timeText);
            add(txt1);
            add(auto_period);
            add(txt2);
            add(moto_period);
            add(txt3);
            add(comboBox);
            add(txt4);
            add(slider);
            add(txt5);
            add(motolifefield);

            comboBox.addActionListener(e -> {
                P1 = (float) comboBox.getSelectedItem();
                System.out.println("P1= " + P1);
            });


            slider.addChangeListener(e -> carLifetime = ((JSlider)e.getSource()).getValue());

            motolifefield.addActionListener(e -> {
            int a;
            try {
                a = Integer.parseInt(motolifefield.getText());
                if (a >= 0)
                    motoLifetime = a;
                else throw new NumberFormatException();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Ошибка: Введено неверное значение!");
                motolifefield.setText(" ");
            }
            window.requestFocus();
        });

            show.addActionListener(e -> {
                labelTime.setVisible(true);
                isTimeAllowed = true;
                mainMenu.UpdateButtons();
            });
            hide.addActionListener(e -> {
                labelTime.setVisible(false);
                isTimeAllowed = false;
                mainMenu.UpdateButtons();
            });

            show_info.addItemListener(e -> {
                isStatsAllowed = ((JCheckBox) e.getItem()).isSelected();

                mainMenu.UpdateButtons();
            });

            start.addActionListener(actionEvent -> {
                resume();
                mainMenu.UpdateButtons();
            });

            stop.addActionListener(actionEvent -> {
                pause();
                mainMenu.UpdateButtons();
            });
            auto_period.addActionListener(e -> {
                int a;
                try {
                    a = Integer.parseInt(auto_period.getText());
                    if (a > 0)
                        N1 = a;
                    else throw new NumberFormatException();
                } catch (NumberFormatException nfe) {
                    N1 = 2;
                    JOptionPane.showMessageDialog(null, "Ошибка: Введено неверное значение!");
                    auto_period.setText(" ");
                }
                window.requestFocus();
            });
            moto_period.addActionListener(e -> {
                int a;
                try {
                    a = Integer.parseInt(moto_period.getText());
                    if (a > 0)
                        N2 = a;
                    else throw new NumberFormatException();
                } catch (NumberFormatException nfe) {
                    N2 = 3;
                    JOptionPane.showMessageDialog(null, "Ошибка: Введено неверное значение!");
                    moto_period.setText(" ");
                }
                window.requestFocus();
            });
        }

        public void UpdateButtons() {
            if (isRunning) {
                start.setEnabled(false);
                stop.setEnabled(true);
            } else {
                start.setEnabled(true);
                stop.setEnabled(false);
            }
            if (isStatsAllowed) {
                show_info.setSelected(true);
            } else {
                show_info.setSelected(false);
            }
            if (isTimeAllowed) {
                show.setSelected(true);
            } else {
                hide.setSelected(true);
            }
        }
    }


    class stats extends JDialog {

        stats() {

            JTextArea textArea = new JTextArea("Cars: " + Car.count + "\nMotorcycles: " + Moto.count + "\nTotal objects: " + (Car.count + Moto.count) + "\nWork Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);

            setTitle("Статистика симуляции");
            textArea.setEditable(false);
            textArea.setFont(new Font("Consolas", Font.PLAIN, 18));

            add(textArea, BorderLayout.NORTH);

            setSize(new Dimension(320, 165));
            setLocationRelativeTo(null);
            setResizable(false);
            setModal(true);

            JButton buttonOK = new JButton("Ок");
            add(buttonOK, BorderLayout.LINE_START);
            buttonOK.setPreferredSize(new Dimension(150, 50));

            buttonOK.addActionListener(e -> {
                window.setImage(null);
                frame.repaint();
                Moto.count = 0;
                Car.count = 0;
                timeFromStart = 0;
                labelTime.setText(timeFromStart / 60 + ":" + "0" + timeFromStart % 60);
                isRunning = false;
                singleton.reset();
                this.dispose();
            });

            JButton buttonCancel = new JButton("Отмена");
            buttonCancel.setPreferredSize(new Dimension(150, 50));
            add(buttonCancel, BorderLayout.LINE_END);

            buttonCancel.addActionListener(e -> {
                resume();
                this.dispose();
            });
            setVisible(true);
        }
    }


    class MenuBar extends JMenuBar {

        JMenuItem startMI = new JMenuItem("Старт");
        JMenuItem stopMI = new JMenuItem("Стоп");
        JMenuItem currentObjs = new JMenuItem("Текущие объекты");
        JRadioButtonMenuItem mshow = new JRadioButtonMenuItem("Показывать время симуляции", true);
        JRadioButtonMenuItem mhide = new JRadioButtonMenuItem("Скрывать время симуляции");
        JCheckBoxMenuItem showinfoMI = new JCheckBoxMenuItem("Показывать информацию");

        MenuBar() {
            JMenu simMenu = new JMenu("Симуляция");

            simMenu.add(startMI);

            simMenu.add(stopMI);
            simMenu.add(currentObjs);
            JMenu settingsMenu = new JMenu("Параметры");

            settingsMenu.add(showinfoMI);
            settingsMenu.addSeparator();
            ButtonGroup timesetMenu = new ButtonGroup();

            timesetMenu.add(mshow);
            timesetMenu.add(mhide);
            mhide.setSelected(true);
            settingsMenu.add(mshow);
            settingsMenu.add(mhide);
            add(simMenu);
            add(settingsMenu);
            setFocusable(true);
            showinfoMI.setSelected(false);
            startMI.addActionListener(e -> resume());
            stopMI.addActionListener(e -> pause());

            showinfoMI.addItemListener(e -> {
                isStatsAllowed = ((JCheckBoxMenuItem) e.getItem()).isSelected();
                sideMenu.UpdateButtons();
            });
            mshow.addActionListener(e -> {
                labelTime.setVisible(true);
                isTimeAllowed = true;
                sideMenu.UpdateButtons();
            });
            mhide.addActionListener(e -> {
                isTimeAllowed = false;
                labelTime.setVisible(false);
                sideMenu.UpdateButtons();
            });
            currentObjs.addActionListener(e -> {pause(); new curObjs();});
        }


        public void UpdateButtons() {
            if (isStatsAllowed) {
                showinfoMI.setSelected(true);
            } else {
                showinfoMI.setSelected(false);
            }

            if (isTimeAllowed) {
                mshow.setSelected(true);
            } else {
                mhide.setSelected(true);
            }
        }

    }

    class curObjs extends JDialog {

        public curObjs() {
            setTitle("Текущие объекты");
            StringBuilder str = new StringBuilder();
            setLocationRelativeTo(frame);
            JTextArea text = new JTextArea(singleton.Map.size(), 20);
            text.setEditable(false);
            text.setBackground(getBackground());
            text.setFont(new Font("Dialog", Font.PLAIN, 16));


            for(Map.Entry<Integer,Long> item : singleton.Map.entrySet()){
                str.append("" + "id: ").append(item.getKey()).append("\t").append("  Время рождения: ").append(item.getValue()).append("\n");
            }
            text.setText(str.toString().substring(0, str.length()-1));

            setSize(350,350 );
            setContentPane(new JScrollPane(text));
            setModal(true);
            setResizable(true);
            setVisible(true);

        }
    }
}




