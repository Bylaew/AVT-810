import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

class Singleton_Vector
{
    private static Singleton_Vector instance;
    private Singleton_Vector(){}
    public static Singleton_Vector getInstance()
    {
        if(instance == null)
            instance = new Singleton_Vector();
        return instance;
    }

    private Vector<Rabbit> vector = new Vector<>();

    public void add(Rabbit rabbit) { vector.add(rabbit); }

    public Rabbit getRabbit(int index) { return vector.get(index); }

    public void remove(int index) { vector.remove(index); }
}

class Singleton_TreeSet
{
    private static Singleton_TreeSet instance;
    private Singleton_TreeSet(){}
    public static Singleton_TreeSet getInstance()
    {
        if(instance == null)
            instance = new Singleton_TreeSet();
        return instance;
    }

    private TreeSet<Integer> ID_tree = new TreeSet<>();

    public void add(Rabbit rabbit)
    {
        while (ID_tree.contains(rabbit.ID)) rabbit.setID((int)(Math.random()*1000));
        ID_tree.add(rabbit.ID);
    }

    public void remove(int id) { ID_tree.remove(id); }
}

class Singleton_HashMap
{
    private static Singleton_HashMap instance;
    private Singleton_HashMap(){}
    public static Singleton_HashMap getInstance()
    {
        if(instance == null)
            instance = new Singleton_HashMap();
        return instance;
    }

    private HashMap<Integer, Long> timeHMap = new HashMap<>();

    public void add(Rabbit rabbit) { timeHMap.put(rabbit.ID, rabbit.birthtime); }

    public void remove(int key) { timeHMap.remove(key); }

    public Set values() { return timeHMap.entrySet(); }
}

public class Habitat
{
    JPanel field = new JPanel();
    BufferedImage fieldImage;
    private static BufferedImage image;
    int width = 1366;
    int height = 768;
    private Timer timer;
    long time = 0;
    boolean sim_is_working = false;
    boolean bool_show_info = false;

    AbstractFactory factory = new RabbitFactory();
    int N1; // Период рождения обычного кролика
    double P1; // Вероятность рождения обычного кролика
    int N2, K; // Период рождения альбиноса и процент от общего количества кроликов
    Singleton_Vector rabbitVector = Singleton_Vector.getInstance();
    Singleton_TreeSet ID_tree = Singleton_TreeSet.getInstance();
    Singleton_HashMap timeHashMap = Singleton_HashMap.getInstance();
    int count;

    AlbinoAI albinoAI = null;
    OrdinaryAI ordinaryAI = null;

    public Habitat()
    {
        N1 = 2000;
        P1 = 0.7;
        N2 = 3000;
        K = 20;
        count = 0;

        try
        {
            image = ImageIO.read(new File("./src/Habitat.jpg"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Rabbits Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel controlPanel = new JPanel();
        JPanel ordAIbuttons = new JPanel();
        JPanel albAIbuttons = new JPanel();
        controlPanel.setLayout(new GridLayout(23, 1, 5, 5));
        Container container = frame.getContentPane();

        JCheckBox show_info = new JCheckBox("Показывать информацию");
        show_info.setSelected(bool_show_info);
        show_info.setFocusable(false);
        JButton start = new JButton("Старт");
        JButton stop = new JButton("Стоп");
        JButton currentRabbits = new JButton("Текущие объекты");
        JButton stopOrdinaryAI = new JButton("Остановка AI \nобычных кроликов");
        JButton resumeOrdinaryAI = new JButton("Запуск AI \nобычных кроликов");
        JButton stopAlbinoAI = new JButton("Остановка AI альбиносов");
        JButton resumeAlbinoAI = new JButton("Запуск AI альбиносов");
        stop.setEnabled(false);
        start.setFocusable(false);
        stop.setFocusable(false);
        currentRabbits.setFocusable(false);
        stopOrdinaryAI.setFocusable(false);
        resumeOrdinaryAI.setFocusable(false);
        stopAlbinoAI.setFocusable(false);
        resumeAlbinoAI.setFocusable(false);
        ButtonGroup sim_time = new ButtonGroup();
        JRadioButton show, hide;
        show = new JRadioButton("Показывать время симуляции", true);
        sim_time.add(show);
        show.setFocusable(false);
        hide = new JRadioButton("Скрывать время симуляции");
        sim_time.add(hide);
        hide.setFocusable(false);
        JTextArea time_text = new JTextArea();
        time_text.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        time_text.setText("Время: " + (time/1000) + " секунд(ы)");
        time_text.setEditable(false);
        time_text.setFocusable(false);
        JTextArea mark1 = new JTextArea();
        mark1.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        mark1.setText("Период рождения обычного кролика");
        mark1.setEditable(false);
        mark1.setFocusable(false);
        JTextArea mark2 = new JTextArea();
        mark2.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        mark2.setText("Период рождения альбиноса");
        mark2.setEditable(false);
        mark2.setFocusable(false);
        JTextArea mark3 = new JTextArea();
        mark3.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        mark3.setText("Вероятность рождения обычного кролика");
        mark3.setEditable(false);
        mark3.setFocusable(false);
        JTextArea mark4 = new JTextArea();
        mark4.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        mark4.setText("Время жизни обычного кролика");
        mark4.setEditable(false);
        mark4.setFocusable(false);
        JTextArea mark5 = new JTextArea();
        mark5.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        mark5.setText("Время жизни альбиноса");
        mark5.setEditable(false);
        mark5.setFocusable(false);
        JTextArea mark6 = new JTextArea();
        mark6.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        mark6.setText("Приоритет AI обычного кролика");
        mark6.setEditable(false);
        mark6.setFocusable(false);
        JTextArea mark7 = new JTextArea();
        mark7.setFont(new Font("TimesRoman", Font.ITALIC, 14));
        mark7.setText("Приоритет AI альбиноса");
        mark7.setEditable(false);
        mark7.setFocusable(false);
        TextField ordinary_period = new TextField("", 10);
        TextField albino_period = new TextField("", 10);
        TextField ordinary_ltime = new TextField("", 10);
        TextField albino_ltime = new TextField("", 10);
        JComboBox<Double> pcb = new JComboBox<>();
        for (int i = 10; i <= 100; i += 10)
            pcb.addItem((double)i/100.0);
        pcb.setFocusable(false);
        JComboBox<Integer> ordPrt = new JComboBox<>();
        for (int i = 1; i <= 10; i++)
            ordPrt.addItem(i);
        ordPrt.setFocusable(false);
        JComboBox<Integer> albPrt = new JComboBox<>();
        for (int i = 1; i <= 10; i++)
            albPrt.addItem(i);
        albPrt.setFocusable(false);

        // Главное меню
        JMenuBar mainMenu = new JMenuBar();
        JMenu simMenu = new JMenu("Симуляция");
        JMenuItem startMI = new JMenuItem("Старт");
        simMenu.add(startMI);
        JMenuItem stopMI = new JMenuItem("Стоп");
        simMenu.add(stopMI);

        JMenu settingsMenu = new JMenu("Параметры");
        JCheckBoxMenuItem showinfoMI = new JCheckBoxMenuItem("Показывать информацию");
        settingsMenu.add(showinfoMI);
        settingsMenu.addSeparator();
        ButtonGroup timesetMenu = new ButtonGroup();
        JRadioButtonMenuItem mshow = new JRadioButtonMenuItem("Показывать время симуляции", true);
        JRadioButtonMenuItem mhide = new JRadioButtonMenuItem("Скрывать время симуляции");
        timesetMenu.add(mshow);
        timesetMenu.add(mhide);
        settingsMenu.add(mshow);
        settingsMenu.add(mhide);
        mainMenu.add(simMenu);
        mainMenu.add(settingsMenu);

        // Слушатели
        show_info.addItemListener(e -> {
            bool_show_info = ((JCheckBox)e.getItem()).isSelected();
            showinfoMI.setSelected(bool_show_info);
        });
        pcb.addItemListener(e -> P1 = (double)pcb.getSelectedItem());
        ordPrt.addItemListener(e -> ordinaryAI.setPriority((int)ordPrt.getSelectedItem()));
        albPrt.addItemListener(e -> albinoAI.setPriority((int)albPrt.getSelectedItem()));
        start.addActionListener(e -> startMethod(container));
        stop.addActionListener(e -> stopMethod(container));
        currentRabbits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog alive = new JDialog();
                alive.setTitle("Текущие объекты");
                alive.setModal(true);
                alive.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                JTextArea textArea = new JTextArea();
                textArea.setFont(new Font("TimesRoman", Font.ITALIC, 25));
                textArea.setText("Список живых объектов:\n" + timeHashMap.values());
                textArea.setEditable(false);
                Container c = alive.getContentPane();
                c.add(textArea);
                alive.pack();
                alive.setVisible(true);
            }
        });
        show.addActionListener(e -> {
            time_text.setVisible(true);
            mshow.setSelected(true);
        });
        hide.addActionListener(e -> {
            time_text.setVisible(false);
        mhide.setSelected(true);
        });
        ordinary_period.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a;
                try
                {
                    a = Integer.parseInt(ordinary_period.getText());
                    if (a > 0)
                        N1 = a;
                    else throw new NumberFormatException();
                }
                catch (NumberFormatException nfe)
                {
                    N1 = 2000;
                    JDialog error = new JDialog();
                    error.setTitle("Ошибка: введено неверное значение");
                    error.setModal(true);
                    error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JTextArea textArea = new JTextArea();
                    textArea.setFont(new Font("TimesRoman", Font.ITALIC, 14));
                    textArea.setText("Значение периода рождения должно быть положительным целочисленным в миллисекундах, \nвведение букв не допускается. \nВыставлено значение по умолчанию: " + N1);
                    textArea.setEditable(false);
                    Container c = error.getContentPane();
                    c.add(textArea);
                    error.pack();
                    error.setVisible(true);
                }
                frame.requestFocus();
            }
        });
        albino_period.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a;
                try
                {
                    a = Integer.parseInt(albino_period.getText());
                    if (a > 0)
                        N2 = a;
                    else throw new NumberFormatException();
                }
                catch (NumberFormatException nfe)
                {
                    N2 = 3000;
                    JDialog error = new JDialog();
                    error.setTitle("Ошибка: введено неверное значение");
                    error.setModal(true);
                    error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JTextArea textArea = new JTextArea();
                    textArea.setFont(new Font("TimesRoman", Font.ITALIC, 14));
                    textArea.setText("Значение периода рождения должно быть положительным целочисленным в миллисекундах, \nвведение букв не допускается. \nВыставлено значение по умолчанию: " + N2);
                    textArea.setEditable(false);
                    Container c = error.getContentPane();
                    c.add(textArea);
                    error.pack();
                    error.setVisible(true);
                }
                frame.requestFocus();
            }
        });
        ordinary_ltime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a;
                try
                {
                    a = Integer.parseInt(ordinary_ltime.getText());
                    if (a > 0)
                        Ordinary_Rabbit.lifetime = a;
                    else throw new NumberFormatException();
                }
                catch (NumberFormatException nfe)
                {
                    Ordinary_Rabbit.lifetime = 10000;
                    JDialog error = new JDialog();
                    error.setTitle("Ошибка: введено неверное значение");
                    error.setModal(true);
                    error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JTextArea textArea = new JTextArea();
                    textArea.setFont(new Font("TimesRoman", Font.ITALIC, 14));
                    textArea.setText("Значение времени жизни должно быть положительным целочисленным в миллисекундах, \nвведение букв не допускается. \nВыставлено значение по умолчанию: " + Ordinary_Rabbit.lifetime);
                    textArea.setEditable(false);
                    Container c = error.getContentPane();
                    c.add(textArea);
                    error.pack();
                    error.setVisible(true);
                }
                frame.requestFocus();
            }
        });
        albino_ltime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a;
                try
                {
                    a = Integer.parseInt(albino_ltime.getText());
                    if (a > 0)
                        Albino.lifetime = a;
                    else throw new NumberFormatException();
                }
                catch (NumberFormatException nfe)
                {
                    Albino.lifetime = 12000;
                    JDialog error = new JDialog();
                    error.setTitle("Ошибка: введено неверное значение");
                    error.setModal(true);
                    error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JTextArea textArea = new JTextArea();
                    textArea.setFont(new Font("TimesRoman", Font.ITALIC, 14));
                    textArea.setText("Значение времени жизни должно быть положительным целочисленным в миллисекундах, \nвведение букв не допускается. \nВыставлено значение по умолчанию: " + Albino.lifetime);
                    textArea.setEditable(false);
                    Container c = error.getContentPane();
                    c.add(textArea);
                    error.pack();
                    error.setVisible(true);
                }
                frame.requestFocus();
            }
        });
        startMI.addActionListener(e -> {
            startMethod(container);
            frame.requestFocus();
        });
        stopMI.addActionListener(e -> {
            stopMethod(container);
            frame.requestFocus();
        });
        showinfoMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bool_show_info = ((JCheckBoxMenuItem)e.getSource()).isSelected();
                show_info.setSelected(bool_show_info);
            }
        });
        mshow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time_text.setVisible(true);
                show.setSelected(true);
            }
        });
        mhide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time_text.setVisible(false);
                hide.setSelected(true);
            }
        });
        stopOrdinaryAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (ordinaryAI)
                {
                    ordinaryAI.stopAnimation();
                }
            }
        });
        resumeOrdinaryAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (ordinaryAI)
                {
                    ordinaryAI.resumeAnimation();
                }
            }
        });
        stopAlbinoAI.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                synchronized (albinoAI)
                {
                    albinoAI.stopAnimation();
                }
            }
        });
        resumeAlbinoAI.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                synchronized (albinoAI)
                {
                    albinoAI.resumeAnimation();
                }
            }
        });

        ordAIbuttons.add(stopOrdinaryAI);
        ordAIbuttons.add(resumeOrdinaryAI);
        albAIbuttons.add(stopAlbinoAI);
        albAIbuttons.add(resumeAlbinoAI);

        controlPanel.add(start); // Номер 0 в контейнере controlPanel (не изменять)
        controlPanel.add(stop); // Номер 1 в контейнере controlPanel (не изменять)
        controlPanel.add(currentRabbits);
        controlPanel.add(show_info);
        controlPanel.add(show);
        controlPanel.add(hide);
        controlPanel.add(time_text);
        controlPanel.add(mark1);
        controlPanel.add(ordinary_period);
        controlPanel.add(mark2);
        controlPanel.add(albino_period);
        controlPanel.add(mark3);
        controlPanel.add(pcb);
        controlPanel.add(mark4);
        controlPanel.add(ordinary_ltime);
        controlPanel.add(mark5);
        controlPanel.add(albino_ltime);
        controlPanel.add(ordAIbuttons);
        controlPanel.add(albAIbuttons);
        controlPanel.add(mark6);
        controlPanel.add(ordPrt);
        controlPanel.add(mark7);
        controlPanel.add(albPrt);

        container.add(controlPanel, BorderLayout.EAST); // Номер 0 в контейнере container (не изменять)
        container.add(field, BorderLayout.CENTER); // Номер 1 в контейнере container (не изменять)

        frame.setJMenuBar(mainMenu);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.requestFocus();

        frame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                super.keyPressed(e);
                int keyCode = e.getKeyCode();
                switch(keyCode)
                {
                    case KeyEvent.VK_B:
                    {
                        startMethod(container);
                        break;
                    }
                    case KeyEvent.VK_E:
                    {
                        stopMethod(container);
                        break;
                    }
                    case KeyEvent.VK_T:
                    {
                        if (time_text.isVisible())
                        {
                            time_text.setVisible(false);
                            hide.setSelected(true);
                            mhide.setSelected(true);
                        }
                        else
                        {
                            time_text.setVisible(true);
                            show.setSelected(true);
                            mshow.setSelected(true);
                        }
                        break;
                    }
                }
            }
        });
    }

    public synchronized void generate(long time, JPanel field)
    {
        for (int i = 0; i < count; i++)
        {
            if ((rabbitVector.getRabbit(i).birthtime + rabbitVector.getRabbit(i).getLifetime()) <= time)
            {
                int id = rabbitVector.getRabbit(i).ID;
                timeHashMap.remove(id);
                ID_tree.remove(id);
                rabbitVector.getRabbit(i).die();
                rabbitVector.remove(i);
                count--;
            }
        }

        if (time % N1 == 0)
        {
            if (Math.random() < P1)
            {
                Rabbit rabbit = factory.createOrdinary((int)(Math.random() * (field.getWidth() - 58)),(int)(Math.random() * (field.getHeight() - 104)), time);
                ID_tree.add(rabbit);
                rabbitVector.add(rabbit);
                timeHashMap.add(rabbit);
                count++;
            }
        }
        if (time % N2 == 0)
        {
            double p = (double) (K * count) / 100;
            if ((double)Albino.count < p)
            {
                Rabbit rabbit = factory.createAlbino((int)(Math.random() * (field.getWidth() - 79)),(int)(Math.random() * (field.getHeight() - 128)), time);
                ID_tree.add(rabbit);
                rabbitVector.add(rabbit);
                timeHashMap.add(rabbit);
                count++;
            }
        }
    }

    public synchronized void draw(Graphics g, JPanel field)
    {
        //Двойная буфферизация в BufferedImage (устранение мерцания)
        int w = field.getWidth(), h = field.getHeight();
        fieldImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics fieldImageGraphics= fieldImage.getGraphics();
        fieldImageGraphics.drawImage(image, 0,0, w, h,null);
        for (int i = 0; i < count; i++)
            fieldImageGraphics.drawImage((rabbitVector.getRabbit(i)).getImage(), (rabbitVector.getRabbit(i)).getX(),(rabbitVector.getRabbit(i)).getY(), null);
        g.drawImage(fieldImage,0,0,w,h,null);
    }

    private synchronized void Update(long time, Graphics g, JPanel field)
    {
        generate(time,field);
        draw(g, field);
    }

    private void startMethod(Container container)
    {
        JPanel controlPanel = (JPanel) container.getComponent(0);
        Component field = container.getComponent(1);
        Component start = controlPanel.getComponent(0);
        Component stop = controlPanel.getComponent(1);
        JTextArea time_text = (JTextArea)controlPanel.getComponent(6);
        if (!sim_is_working)
        {
            sim_is_working = true;
            start.setEnabled(false);
            stop.setEnabled(true);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Update(time, field.getGraphics(), (JPanel) field);
                    time += 1000;
                    time_text.setText("Время: " + (time/1000) + " секунд(ы)");
                }
            },1000, 1000);
            if (ordinaryAI == null)
            {
                ordinaryAI = new OrdinaryAI(this);
                ordinaryAI.going = true;
                ordinaryAI.start();
            }
            if (albinoAI == null)
            {
                albinoAI = new AlbinoAI(this);
                albinoAI.going = true;
                albinoAI.start();
            }
        }
    }

    private void stopMethod(Container container)
    {
        JPanel controlPanel = (JPanel) container.getComponent(0);
        Component start = controlPanel.getComponent(0);
        Component stop = controlPanel.getComponent(1);
        if (sim_is_working)
        {
            timer.cancel();
            sim_is_working = false;
            start.setEnabled(true);
            stop.setEnabled(false);
            if (ordinaryAI != null)
            {
                ordinaryAI.going = false;
                ordinaryAI = null;
            }
            if (albinoAI != null)
            {
                albinoAI.going = false;
                albinoAI = null;
            }
            if (bool_show_info)
            {
                JDialog info = new JDialog();
                info.setTitle("Информация о симуляции");
                info.setModal(true);
                JTextArea textArea = new JTextArea();
                textArea.setFont(new Font("TimesRoman", Font.ITALIC, 25));
                textArea.setText("Всего кроликов: " + count +
                        "\nОбычных: " + Ordinary_Rabbit.count +
                        "\nАльбиносов: " + Albino.count +
                        "\nВремя: " + (time/1000) + " секунд(ы)");
                textArea.setEditable(false);
                JButton OK = new JButton("ОК");
                JButton cancel = new JButton("Отмена");
                OK.addActionListener(e -> info.dispose());
                cancel.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        startMethod(container);
                        info.dispose();
                    }
                });
                JPanel bottomPanel = new JPanel();
                bottomPanel.add(OK);
                bottomPanel.add(cancel);
                Container c = info.getContentPane();
                c.add(bottomPanel, BorderLayout.SOUTH);
                c.add(textArea, BorderLayout.CENTER);
                info.pack();
                info.setVisible(true);
            }
        }
    }
}
