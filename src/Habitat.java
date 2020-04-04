import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    private  Vehicle[] array = new Vehicle[1000];

    public void add(Vehicle vehicle, int count)
    {
        array[count] = vehicle;
    }

    public Vehicle get(int i)
    {
        return array[i];
    }
    public void reset(){
        array = new Vehicle[1000];
    }
}

public class Habitat {
    private int width;
    private int height;
    Singleton vehicles = Singleton.getInstance();
    int count = 0;
    private Timer startTime;
    private long timeFromStart = 0;
    private float N1, N2, P1, P2;
    private CarFactory carFactory;
    private MotoFactory motoFactory;
    private boolean isRunning = false;
    private boolean isStatsAllowed;
    private boolean isTimeAllowed;
    private JLabel labelTime;
    SideMenu sideMenu;
    MenuBar mainMenu;
    JFrame frame;


    public void init(float N1, float N2, float P1, float P2) {
        height = 900;
        width = 1600;
        carFactory = new CarFactory();
        motoFactory = new MotoFactory();
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.P2 = P2;

        frame = new JFrame("Lab 2");

        JPanel window = new JPanel();
        frame.add(window, BorderLayout.CENTER);

        sideMenu = new SideMenu();
        frame.add(sideMenu, BorderLayout.EAST);

        mainMenu = new MenuBar();
        frame.setJMenuBar(mainMenu);

        frame.setPreferredSize(new Dimension(width, height));

        JPanel panel = new JPanel();
        labelTime = new JLabel();
        labelTime.setText("0:00");
        labelTime.setVisible(false);
        labelTime.setFont(new Font("Consolas", Font.PLAIN, 60));
        panel.add(labelTime);
        window.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.requestFocus();

        startTime = new Timer(1000, e -> {
            timeFromStart++;
            labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
            update(timeFromStart, window);
            sideMenu.timeText.setText("Время: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60+ " секунд(ы)");
        });


        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    resume();
                }
                    else if (e.getKeyCode() == KeyEvent.VK_E) {
                    pause();

                }
                else if (e.getKeyCode() == KeyEvent.VK_T) {
                        if (labelTime.isVisible())
                        {labelTime.setVisible(false); isTimeAllowed = false;}
                        else {labelTime.setVisible(true); isTimeAllowed = true;}
                }
                else if (e.getKeyCode()== KeyEvent.VK_P){
                    if (isRunning) {pause();}
                    else {resume();}
                    sideMenu.UpdateButtons();
                }
                sideMenu.UpdateButtons();
                mainMenu.UpdateButtons();
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
    }

    private void pause(){
        startTime.stop();
        isRunning = false;
        sideMenu.UpdateButtons();
        mainMenu.UpdateButtons();
        if (isStatsAllowed) {
            new stats(frame);
        }
    }
    private void resume(){
        if (!isRunning) {
            isRunning = true;
            System.out.println("Start");
            startTime.start();
            //vehicles = new ArrayList<>();
           vehicles.reset();
           count = 0;
        }
        startTime.start();
        isRunning = true;
        sideMenu.UpdateButtons();
        mainMenu.UpdateButtons();
    }


    private void update(float time, JPanel window) {
        if (time % N1 == 0) {
            if (Math.random() < P1) {

                vehicles.add(carFactory.createVehicle((float) (Math.random() * (width-400)), (float) Math.random() * (height-200) + 50 ), count);
                count++;
            }
        }
        if (time % N2 == 0) {
            if (Math.random() < P2) {
                vehicles.add(motoFactory.createVehicle((float) Math.random() * (width - 400), (float) Math.random() * (height - 200) + 50 ), count);
                count++;
            }
        }
        for(int i = 0; i < count; i++) {window.getGraphics().drawImage(vehicles.get(i).getImage(), (int) vehicles.get(i).getX(), (int)vehicles.get(i).getY(), vehicles.get(i).getImage().getWidth(null),  vehicles.get(i).getImage().getHeight(null), null);}
    }

    class SideMenu extends JPanel {
        JButton start = new JButton("Старт");
        JButton stop = new JButton("Стоп");
        JCheckBox show_info;
        JRadioButton show, hide;
        JTextArea timeText;


        SideMenu() {
            setLayout(new GridLayout(12, 1));
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
            timeText.setText("Время: " + (timeFromStart / 1000) + " секунд(ы)");
            timeText.setEditable(false);
            timeText.setFocusable(false);
            JTextArea txt1 = new JTextArea();
            txt1.setFont(new Font("TimesRoman", Font.ITALIC, 14));
            txt1.setText("Период появления автомобилей ");
            txt1.setEditable(false);
            txt1.setFocusable(false);
            JTextArea txt2 = new JTextArea();
            txt2.setFont(new Font("TimesRoman", Font.ITALIC, 14));
            txt2.setText("Период появления мотоциклов");
            txt2.setEditable(false);
            txt2.setFocusable(false);
            JTextArea txt3 = new JTextArea();
            txt3.setFont(new Font("TimesRoman", Font.ITALIC, 14));
            txt3.setText("Вероятность появления атомобилкей");
            txt3.setEditable(false);
            txt3.setFocusable(false);
            TextField auto_period = new TextField("", 10);
            auto_period.setEnabled(true);
            TextField moto_period = new TextField("", 10);
            moto_period.setEnabled(true);
            var comboBox = new JComboBox();
            for (int i = 10; i <= 100; i += 10)
                comboBox.addItem((float) i / 100.0f);
            comboBox.setFocusable(true);
            comboBox.requestFocus();

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

            comboBox.addActionListener(e -> {P1 = (float) comboBox.getSelectedItem();
                System.out.println("P1= " + P1); });


            show.addActionListener(e -> {labelTime.setVisible(true); isTimeAllowed = true; mainMenu.UpdateButtons();});
            hide.addActionListener(e-> {labelTime.setVisible(false); isTimeAllowed = false; mainMenu.UpdateButtons();});

            show_info.addItemListener(e -> {
                isStatsAllowed = ((JCheckBox)e.getItem()).isSelected();

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
                try
                {
                    a = Integer.parseInt(auto_period.getText());
                    if (a > 0)
                        N1 = a;
                    else throw new NumberFormatException();
                    frame.requestFocus();
                }
                catch (NumberFormatException nfe)
                {
                    N1 = 2;
                    JOptionPane.showMessageDialog(null, "Ошибка: Введено неверное значение!");
                    auto_period.setText(" ");
                }
            });
            moto_period.addActionListener(e -> {
                int a;
                try
                {
                    a = Integer.parseInt(moto_period.getText());
                    if (a > 0)
                        N2 = a;
                    else throw new NumberFormatException();
                    frame.requestFocus();
                }
                catch (NumberFormatException nfe)
                {
                    N2 = 3;
                    JOptionPane.showMessageDialog(null, "Ошибка: Введено неверное значение!");
                    moto_period.setText(" ");
                }
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
            if (isStatsAllowed){
                show_info.setSelected(true);
            } else {show_info.setSelected(false);}
            if (isTimeAllowed){
                show.setSelected(true);
            } else {hide.setSelected(true);}
        }
    }

    class stats extends  JDialog{


        stats(JFrame frame){

            JDialog dialog = new JDialog(frame, "Statistics");

            var textArea = new JTextArea("Cars: " + Car.count+ "\nMotorcycles: "+ Moto.count+"\nTotal objects: " + (Car.count + Moto.count)+"\nWork Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);


            textArea.setEditable(false);
            textArea.setFont(new Font("Consolas",Font.PLAIN, 18));

            dialog.setLayout(new BorderLayout());
            dialog.add(textArea, BorderLayout.NORTH);

            dialog.setSize(new Dimension(320, 165));
            dialog.setLocationRelativeTo(null);
            dialog.setResizable(false);
            dialog.setModal(true);

            JButton buttonOK = new JButton("Ок");
            dialog.add(buttonOK, BorderLayout.LINE_START);
            buttonOK.setPreferredSize(new Dimension(150, 50));

            buttonOK.addActionListener(e -> {
                frame.repaint();
                Moto.count = 0;
                Car.count = 0;
                timeFromStart = 0;
                labelTime.setText(timeFromStart / 60 + ":" + "0" + timeFromStart % 60);
                isRunning = false;
                vehicles.reset();
                count = 0;
                dialog.dispose();});

            JButton buttonCancel = new JButton("Отмена");
            buttonCancel.setPreferredSize(new Dimension(150, 50));
            dialog.add(buttonCancel, BorderLayout.LINE_END);

            buttonCancel.addActionListener(e -> {resume(); dialog.dispose();});
            dialog.setVisible(true);
        }
    }


    class MenuBar extends JMenuBar{

        JMenuItem startMI = new JMenuItem("Старт");
        JMenuItem stopMI = new JMenuItem("Стоп");
        JRadioButtonMenuItem mshow = new JRadioButtonMenuItem("Показывать время симуляции", true);
        JRadioButtonMenuItem mhide = new JRadioButtonMenuItem("Скрывать время симуляции");
        JCheckBoxMenuItem showinfoMI = new JCheckBoxMenuItem("Показывать информацию");

        MenuBar() {
            JMenu simMenu = new JMenu("Симуляция");

            simMenu.add(startMI);

            simMenu.add(stopMI);
            JMenu settingsMenu = new JMenu("Параметры");

            settingsMenu.add(showinfoMI);
            settingsMenu.addSeparator();
            ButtonGroup timesetMenu = new ButtonGroup();

            timesetMenu.add(mshow);
            timesetMenu.add(mhide);
            mhide.setSelected(true);
            settingsMenu.add(mshow);
            settingsMenu.add(mhide);
            this.add(simMenu);
            this.add(settingsMenu);
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
        }
            public void UpdateButtons(){
                if (isStatsAllowed){
                    showinfoMI.setSelected(true);
                } else {showinfoMI.setSelected(false);}

                if (isTimeAllowed){
                    mshow.setSelected(true);
                } else {mhide.setSelected(true);}
            }

    }
}




