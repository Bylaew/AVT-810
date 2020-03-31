import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

class Singleton
{
    private static Singleton instance;
    private Singleton(){}
    public static Singleton getInstance()
    {
        if(instance == null)
            instance = new Singleton();
        return instance;
    }

    private Rabbit[] array = new Rabbit[1000];

    public void add(Rabbit rabbit, int count)
    {
        array[count] = rabbit;
    }

    public Rabbit getRabbit(int i)
    {
        return array[i];
    }
}

public class Habitat
{
    BufferedImage fieldImage;
    private static BufferedImage image;
    private int width = 1280;
    private int height = 720;
    private Timer timer;
    private long time = 0;
    boolean sim_is_working = false;
    boolean bool_show_info = false;

    AbstractFactory factory = new RabbitFactory();
    int N1; // Период рождения обычного кролика
    double P1; // Вероятность рождения обычного кролика
    int N2, K; // Период рождения альбиноса и процент от общего количества кроликов
    Singleton array = Singleton.getInstance();
    int count;

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
        JPanel field = new JPanel();
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(12, 1, 5, 12));
        Container container = frame.getContentPane();

        JCheckBox show_info = new JCheckBox("Показывать информацию");
        show_info.setSelected(bool_show_info);
        show_info.setFocusable(false);
        JButton start = new JButton("Старт");
        JButton stop = new JButton("Стоп");
        stop.setEnabled(false);
        start.setFocusable(false);
        stop.setFocusable(false);
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
        TextField ordinary_period = new TextField("", 10);
        TextField albino_period = new TextField("", 10);
        JComboBox pcb = new JComboBox();
        for (int i = 10; i <= 100; i += 10)
            pcb.addItem((double)i/100.0);
        pcb.setFocusable(false);

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
        start.addActionListener(e -> startMethod(container));
        stop.addActionListener(e -> stopMethod(container));
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
                    frame.requestFocus();
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
                    frame.requestFocus();
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

        controlPanel.add(start); // Номер 0 в контейнере controlPanel (не изменять)
        controlPanel.add(stop); // Номер 1 в контейнере controlPanel (не изменять)
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

    private void Update(long time, Graphics g, JPanel field)
    {
        if (time % N1 == 0)
        {
            if (Math.random() < P1)
            {
                array.add(factory.createOrdinary((float)(Math.random() * (field.getWidth() - 58)),(float)(Math.random() * (field.getHeight() - 104))), count);
                count++;
            }
        }
        if (time % N2 == 0)
        {
            double p = (double) (K * count) / 100;
            if ((double)Albino.count < p)
            {
                array.add(factory.createAlbino((float)(Math.random() * (field.getWidth() - 79)), (float)(Math.random() * (field.getHeight() - 128))), count);
                count++;
            }
        }

        //Двойная буфферизация в BufferedImage (устранение мерцания)
        int w = field.getWidth(), h = field.getHeight();
        fieldImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics fieldImageGraphics= fieldImage.getGraphics();
        fieldImageGraphics.drawImage(image, 0,0, w, h,null);
        for (int i = 0; i < count; i++)
            fieldImageGraphics.drawImage((array.getRabbit(i)).getImage(), (int)((array.getRabbit(i)).getX()),(int)((array.getRabbit(i)).getY()), null);
        g.drawImage(fieldImage,0,0,w,h,null);
    }

    private void startMethod(Container container)
    {
        JPanel controlPanel = (JPanel) container.getComponent(0);
        Component field = container.getComponent(1);
        Component start = controlPanel.getComponent(0);
        Component stop = controlPanel.getComponent(1);
        JTextArea time_text = (JTextArea)controlPanel.getComponent(5);
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
