import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Habitat {
    Singleton singleton = Singleton.getInstance();
    SideMenu sideMenu;
    MenuBar mainMenu;
    JFrame frame;
    ImagePanel window;
    CarAI carAI = new CarAI(this);
    MotoAI motoAI = new MotoAI(this);
    Client client;
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
    private long carLifetime = 1500, motoLifetime = 500;

    public void init(float N1, float N2, float P1, float P2) {



        concreteFactory = new ConcreteFactory();
        carAI.start();
        motoAI.start();

        this.N1 = N1 * 100;
        this.N2 = N2 * 100;
        this.P1 = P1;
        this.P2 = P2;

        readConfig();

        frame = new JFrame("Lab 6");


        window = new ImagePanel();
        frame.add(window, BorderLayout.CENTER);

        sideMenu = new SideMenu();
        frame.add(sideMenu, BorderLayout.EAST);

        mainMenu = new MenuBar();
        frame.setJMenuBar(mainMenu);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double w = screenSize.getWidth();
        double h = screenSize.getHeight();
        window.setPreferredSize(new Dimension((int) (w / 2.5), (int) (h / 2.5)));


        labelTime = new JLabel();
        labelTime.setText("0:00");
        labelTime.setVisible(false);
        labelTime.setFont(new Font("Consolas", Font.PLAIN, 45));
        window.add(labelTime);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setResizable(true);
        frame.pack();


        startTime = new Timer(10, e -> {
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

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveConfig();
                super.windowClosing(e);
            }
        });

        sideMenu.UpdateButtons();
        mainMenu.UpdateButtons();
        frame.setVisible(true);
        window.requestFocus();
        width = window.getWidth();
        height = window.getHeight();

        client = new Client(Habitat.this);
        sideMenu.swapbutton.addActionListener(e->{
            int id = (int) sideMenu.connections.getSelectedItem();
            System.out.println("Кнопка нажата");
            client.send_objects(id);
        });

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


    private synchronized void update(float time, ImagePanel window) {
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

        /*
        for (int i = 0; i < singleton.vehicles.size(); i++) {
            if (singleton.vehicles.get(i).getLifeTime() + singleton.vehicles.get(i).getBirthTime() <= timeFromStart) {
                singleton.Map.remove(singleton.vehicles.get(i).getId());
                singleton.idSet.remove(singleton.vehicles.get(i).getId());
                singleton.vehicles.remove(i);

            }
        }*/
        Vehicle cur;
        Iterator<Vehicle> iter = singleton.vehicles.iterator();
        synchronized (iter) {
            while (iter.hasNext()) {
                cur = iter.next();
                if (cur.getLifeTime() + cur.getBirthTime() <= timeFromStart) {
                    singleton.Map.remove(cur.getId());
                    singleton.idSet.remove(cur.getId());
                    iter.remove();
                }
            }
        }
        redraw();
    }

    public void redraw() {
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

    private void saveConfig() {
        try (FileWriter fw = new FileWriter("config.txt")) {
            fw.write(N1 + "\n" + P1 + "\n" + N2 + "\n");
            fw.write((isStatsAllowed) + "\n");
            fw.write((isTimeAllowed) + "\n");
            fw.write((carLifetime) + "\n");
            fw.write((motoLifetime) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readConfig() {
        try (FileReader fw = new FileReader("config.txt")) {
            Scanner scanner = new Scanner(fw);
            if (scanner.hasNextLine()) N1 = Float.parseFloat(scanner.nextLine());
            if (scanner.hasNextLine()) P1 = Float.parseFloat(scanner.nextLine());
            if (scanner.hasNextLine()) N2 = Float.parseFloat(scanner.nextLine());
            if (scanner.hasNextLine()) isStatsAllowed = Boolean.parseBoolean(scanner.nextLine());
            if (scanner.hasNextLine()) isTimeAllowed = Boolean.parseBoolean(scanner.nextLine());
            if (scanner.hasNextLine()) carLifetime = Long.parseLong(scanner.nextLine());
            if (scanner.hasNextLine()) motoLifetime = Long.parseLong(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reduceMoto(int percent) {
        int count = 0;
        for (Vehicle moto : singleton.vehicles) {
            if (moto instanceof Moto) {
                count++;
            }
        }
        System.out.println("Количество мотоциклов: " + count);
        int removenumber = (count * percent) / 100;
        System.out.println("Подлежит удалению: " + removenumber);

        Iterator<Vehicle> iter = singleton.vehicles.iterator();

        while (iter.hasNext() & removenumber > 0) {
            Vehicle current = iter.next();
            if (current instanceof Moto) {
                iter.remove();
                removenumber--;
            }
        }
        redraw();
    }

    class SideMenu extends JPanel {
        JPanel left = new JPanel();
        JPanel right = new JPanel();
        JButton start = new JButton("Старт");
        JButton stop = new JButton("Стоп");
        JCheckBox show_info;
        JRadioButton show, hide;
        JTextArea timeText;
        JComboBox<Float> comboBox = new JComboBox<>();
        JComboBox<Integer> connections = new JComboBox<>();
        JButton swapbutton = new JButton("Обмен");


        SideMenu() {

            // ----------------- LEFT SIDE -----------\\
            left.setLayout(new GridLayout(16, 1));
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
            auto_period.setText(String.valueOf((int) N1 / 100));
            TextField moto_period = new TextField("", 10);
            moto_period.setEnabled(true);
            moto_period.setText(String.valueOf((int) N2 / 100));

            for (int i = 10; i <= 100; i += 10)
                comboBox.addItem((float) i / 100.0f);
            comboBox.setSelectedItem(P1);


            JLabel txt4 = new JLabel("Время жизни автомобилей");
            JSlider slider = new JSlider(0, 50, (int) carLifetime / 100);
            slider.setMajorTickSpacing(10);
            slider.setMajorTickSpacing(5);
            slider.setPaintLabels(true);
            slider.setPaintTicks(true);
            slider.setSnapToTicks(true);

            JLabel txt5 = new JLabel("Время жизни мотоциклов");
            TextField motolifefield = new TextField("", 10);
            motolifefield.setText("" + motoLifetime / 100);

            left.add(start);
            left.add(stop);
            left.add(show_info);
            left.add(show);
            left.add(hide);
            left.add(timeText);
            left.add(txt1);
            left.add(auto_period);
            left.add(txt2);
            left.add(moto_period);
            left.add(txt3);
            left.add(comboBox);
            left.add(txt4);
            left.add(slider);
            left.add(txt5);
            left.add(motolifefield);
            // ----------------------------\\


            // ----------------- RIGHT SIDE -----------\\
            right.setLayout(new GridLayout(16, 1));
            JButton CarStopButton = new JButton("Остановить автомобили");
            JButton CarResumeButton = new JButton("Разбудить автомобили");
            JButton MotoStopButton = new JButton("Остановить мотоциклы");
            JButton MotoResumeButton = new JButton("Разбудить мотоцилы");
            CarResumeButton.setEnabled(false);
            MotoResumeButton.setEnabled(false);
            JComboBox<Integer> CarPriority = new JComboBox<>();
            JComboBox<Integer> MotoPriority = new JComboBox<>();
            CarPriority.setEditable(false);
            MotoPriority.setEditable(false);
            right.add(CarResumeButton);
            right.add(CarStopButton);
            right.add(MotoResumeButton);
            right.add(MotoStopButton);
            CarResumeButton.addActionListener(e -> {
                carAI.resumeAI();
                CarResumeButton.setEnabled(false);
                CarStopButton.setEnabled(true);
            });
            CarStopButton.addActionListener(e -> {
                carAI.pauseAI();
                CarResumeButton.setEnabled(true);
                CarStopButton.setEnabled(false);
            });
            MotoResumeButton.addActionListener(e -> {
                motoAI.resumeAI();
                MotoResumeButton.setEnabled(false);
                MotoStopButton.setEnabled(true);
            });
            MotoStopButton.addActionListener(e -> {
                motoAI.pauseAI();
                MotoResumeButton.setEnabled(true);
                MotoStopButton.setEnabled(false);
            });

            for (int i = 1; i <= 2; i++) {
                CarPriority.addItem(i);
                MotoPriority.addItem(i);
            }
            CarPriority.addItemListener(e -> carAI.setPriority((int) CarPriority.getSelectedItem()));
            MotoPriority.addItemListener(e -> carAI.setPriority((int) CarPriority.getSelectedItem()));
            right.add(new JLabel("Приоритет потока автомобилей"));
            right.add(CarPriority);
            right.add(new JLabel("Приоритет потока Мотоциклов"));
            right.add(MotoPriority);
            right.add(new JLabel("Список доступных клиентов:"));
            right.add(connections);
            right.add(swapbutton);


            /*swapbutton.addActionListener(e -> {

                //int id = (int) connections.getSelectedItem();
                //System.out.println("Выбран элемент: " + id);
                if (client == null) System.out.println("Ты дебил");
                client.send_objects(100);
            }); */
            // ----------------------------\\


            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(left);
            add(right);


            comboBox.addActionListener(e -> {
                P1 = (float) comboBox.getSelectedItem();
                System.out.println("P1= " + P1);
            });


            slider.addChangeListener(e -> carLifetime = ((JSlider) e.getSource()).getValue() * 100);

            motolifefield.addActionListener(e -> {
                int a;
                try {
                    a = Integer.parseInt(motolifefield.getText());
                    if (a >= 0)
                        motoLifetime = a * 100;
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
                        N1 = a * 100;
                    else throw new NumberFormatException();
                } catch (NumberFormatException nfe) {
                    N1 = 2 * 100;
                    JOptionPane.showMessageDialog(null, "Ошибка: Введено неверное значение!");
                    auto_period.setText("");
                }
                window.requestFocus();
            });
            moto_period.addActionListener(e -> {
                int a;
                try {
                    a = Integer.parseInt(moto_period.getText());
                    if (a > 0)
                        N2 = a * 100;
                    else throw new NumberFormatException();
                } catch (NumberFormatException nfe) {
                    N2 = 3 * 100;
                    JOptionPane.showMessageDialog(null, "Ошибка: Введено неверное значение!");
                    moto_period.setText("");
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
            if (isStatsAllowed & !show_info.isSelected()) {
                show_info.doClick();
            } else if (!isStatsAllowed & show_info.isSelected()) {
                show_info.doClick();
            }
            if (isTimeAllowed) {
                show.doClick();
            } else {
                hide.doClick();
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
        JMenuItem consoleButton = new JMenuItem("Консоль");
        JMenuItem stopMI = new JMenuItem("Стоп");
        JMenu FileMenu = new JMenu("Файл");
        JMenuItem currentObjs = new JMenuItem("Текущие объекты");
        JRadioButtonMenuItem mshow = new JRadioButtonMenuItem("Показывать время симуляции", true);
        JRadioButtonMenuItem mhide = new JRadioButtonMenuItem("Скрывать время симуляции");
        JCheckBoxMenuItem showinfoMI = new JCheckBoxMenuItem("Показывать информацию");

        MenuBar() {
            JMenu simMenu = new JMenu("Симуляция");

            simMenu.add(startMI);
            simMenu.add(stopMI);
            simMenu.add(currentObjs);
            simMenu.add(consoleButton);
            JMenu settingsMenu = new JMenu("Параметры");

            settingsMenu.add(showinfoMI);
            settingsMenu.addSeparator();
            ButtonGroup timesetMenu = new ButtonGroup();

            JMenuItem save = new JMenuItem("Сохранить объекты");
            JMenuItem load = new JMenuItem("Загрузить объекты");
            FileMenu.add(save);
            FileMenu.add(load);
            timesetMenu.add(mshow);
            timesetMenu.add(mhide);
            mhide.setSelected(true);
            settingsMenu.add(mshow);
            settingsMenu.add(mhide);
            add(FileMenu);
            add(simMenu);
            add(settingsMenu);
            setFocusable(true);
            showinfoMI.setSelected(false);
            startMI.addActionListener(e -> resume());
            stopMI.addActionListener(e -> pause());

            consoleButton.addActionListener(e -> {
                Console console = new Console(Habitat.this);
            });

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
            save.addActionListener(e -> {
                pause();
                String cwd = new File("").getAbsolutePath();
                JFileChooser saveDialog = new JFileChooser(cwd);
                saveDialog.showOpenDialog(frame);
                File selFile = saveDialog.getSelectedFile();
                if (selFile != null) {
                    try {
                        singleton.saveObj(selFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                resume();
            });
            load.addActionListener(e -> {
                pause();
                String cwd = new File("").getAbsolutePath();
                JFileChooser loadDialog = new JFileChooser(cwd);
                loadDialog.showOpenDialog(frame);
                File selFile = loadDialog.getSelectedFile();
                if (selFile != null) {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(
                                new FileInputStream(selFile));
                        singleton = (Singleton) ois.readObject();
                        for (Vehicle vehicle : singleton.vehicles) {
                            vehicle.setBirthTime(timeFromStart);
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                redraw();
            });
            currentObjs.addActionListener(e -> {
                pause();
                new curObjs();
            });
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


            for (Map.Entry<Integer, Long> item : singleton.Map.entrySet()) {
                str.append("" + "id: ").append(item.getKey()).append("\t").append("  Время рождения: ").append(item.getValue()).append("\n");
            }
            text.setText(str.toString().substring(0, str.length() - 1));

            setSize(350, 350);
            setContentPane(new JScrollPane(text));
            setModal(true);
            setResizable(true);
            setVisible(true);

        }
    }
}




