import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private House[] array = new House[1000];

    public void add(House house, int i)
    {
        array[i] = house;
    }

    public House getHouse(int i)
    {
        return array[i];
    }
}


public class Habitat
{
    //private Image background;
    private int width;
    private int height;
    private ConcreteFactory allFactory;
    boolean simulation=false;
    private Timer timer;
    private float time;
    Singleton array =Singleton.getInstance();
    private int counter;

    //условия: вероятности и время
    private float t1,t2;
    private double p1,p2;
   public class UserInterface
    {
       private JFrame frame = new JFrame("Simulation");
        JPanel simulation_panel = new JPanel(); // разделение фрейма на область симуляциии и настроек
        JPanel settings_panel = new JPanel();
        JTextArea timerInd = new JTextArea();
        //панель инструментов
        JMenuBar menubar = new JMenuBar();
            JMenu menu = new JMenu("Menu");
                JMenuItem startMenuItem = new JMenuItem("Start");
                JMenuItem stopMenuItem = new JMenuItem("Stop");
                JCheckBoxMenuItem showDialogMenuItem = new JCheckBoxMenuItem("Show dialog");
                JMenu timeItemMenu = new JMenu("Time");
                    ButtonGroup timeItemMenuGroup = new ButtonGroup();
                        JRadioButtonMenuItem showTimeMenuItem = new JRadioButtonMenuItem("Show time");
                        JRadioButtonMenuItem hideTimeMenuItem = new JRadioButtonMenuItem("Hide time");

        //кнопки
        JPanel onPanel = new JPanel();
            ButtonGroup onGroup = new ButtonGroup();
                JToggleButton start_button = new JToggleButton("Start simulation");
                JToggleButton stop_button = new JToggleButton("Stop simulation");
        //чекбокс
        JCheckBox showDialogBox = new JCheckBox("Show dialog");
        //связанные переключатели
        JPanel timeSwitchPanel = new JPanel();
        ButtonGroup bg= new ButtonGroup();
        JRadioButton showTimeButton= new JRadioButton("Show time");
        JRadioButton hideTimeButton= new JRadioButton("Hide time");
        //комбобокс
        JComboBox cmb1 = new JComboBox();
        JComboBox cmb2 = new JComboBox();
        //текстовые зоны
        JTextField period1 = new JTextField();
        JTextField period2 = new JTextField();

        UserInterface(){
            frame.add(settings_panel,BorderLayout.WEST);// настройки слева
            frame.add(simulation_panel,BorderLayout.CENTER);// симуляция справа

            settings_panel.setBackground(Color.LIGHT_GRAY);
            settings_panel.add(timerInd);

            timerInd.setVisible(true);
            timerInd.setFont(new Font("Helvetica",Font.BOLD,14));
            timerInd.setEditable(false);

            //добавление элементов к верхней панели инструментов
            menubar.add(menu);
                menu.add(startMenuItem);
                menu.add(stopMenuItem);
                timeItemMenuGroup.add(showTimeMenuItem);
                timeItemMenuGroup.add(hideTimeMenuItem);
            menu.add(timeItemMenu);
                timeItemMenu.add(showTimeMenuItem);
                timeItemMenu.add(hideTimeMenuItem);
            //menu.add(showTimeMenuItem);
            //menu.add(hideTimeMenuItem);

                menu.add(showDialogMenuItem);
            frame.setJMenuBar(menubar);

            //добавление кнопок
            onPanel.add(start_button);
            onPanel.add(stop_button);
            onGroup.add(start_button);
            onGroup.add(stop_button);

            //связанные переключатели
            timeSwitchPanel.add(showTimeButton);
            timeSwitchPanel.add(hideTimeButton);
            bg.add(showTimeButton);
            bg.add(hideTimeButton);

            //настройки комобоксов
            cmb1.setEditable(false);
            cmb2.setEditable(false);

            for (int i = 1; i < 10;i=i+1 )
            {
                cmb1.addItem((double)i/10);
                cmb2.addItem((double)i/10);
            }
            //добавление элементов к панели настроек
            settings_panel.setLayout(new GridLayout(12,1,0,4));
            settings_panel.add(onPanel);
            settings_panel.add(showDialogBox);

            showDialogBox.setSelected(true);
            //settings_panel.add(text);
            settings_panel.add(new JLabel("Probability 1"));
            settings_panel.add(cmb1);
            settings_panel.add(new JLabel("Probability 2"));
            settings_panel.add(cmb2);
            settings_panel.add(timeSwitchPanel);
            settings_panel.add(new JLabel("Period 1"));
            settings_panel.add(period1);
            settings_panel.add(new JLabel("Period 2"));
            settings_panel.add(period2);

            simulation_panel.setVisible(true);
            settings_panel.setVisible(true);};

    }
    Habitat()
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        UserInterface ui = new UserInterface();
        this.height=720;
        this.width=1280;
        allFactory= new ConcreteFactory();
        //JFrame frame = new JFrame("Simulation");
        ui.frame.setSize(width,height);
        ui.frame.setVisible(true);
       // frame.setResizable(false);
        counter =0;t1=2;t2=3;p1=0.8;p2=0.7;
        time=0;


        //слушатели
        ui.simulation_panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ui.simulation_panel.requestFocus();
                //super.mouseClicked(e);
            }
        });
        ui.start_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                startSimulation(ui);
            }

        });
        ui.startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                startSimulation(ui);
            }

        });

        ui.stop_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseSimulation(ui);
            }

        });
        ui.stopMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseSimulation(ui);
            }

        });

        ui.showDialogMenuItem.setSelected(true);
        ui.showDialogMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {ui.showDialogBox.setSelected(true);}
                else
                {ui.showDialogBox.setSelected(false);}
            }
        });
        ui.showDialogBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {ui.showDialogMenuItem.setSelected(true);}
                else
                {ui.showDialogMenuItem.setSelected(false);}
            }
        });

        ui.showTimeMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {ui.showTimeButton.setSelected(true);ui.timerInd.setVisible(true);}
                if (e.getStateChange()== ItemEvent.DESELECTED)
                {ui.showTimeButton.setSelected(false);ui.timerInd.setVisible(false);}
            }
        });
        ui.showTimeButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {ui.showTimeMenuItem.setSelected(true);ui.timerInd.setVisible(true);}
                if (e.getStateChange()== ItemEvent.DESELECTED)
                {ui.showTimeMenuItem.setSelected(false);ui.timerInd.setVisible(false);}
            }
        });

        ui.hideTimeMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {ui.hideTimeButton.setSelected(true);ui.timerInd.setVisible(false);}
                if (e.getStateChange()== ItemEvent.DESELECTED)
                {ui.hideTimeButton.setSelected(false);ui.timerInd.setVisible(true);}
            }
        });
        ui.hideTimeButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()== ItemEvent.SELECTED)
                {ui.hideTimeMenuItem.setSelected(true);ui.timerInd.setVisible(false);}
                if (e.getStateChange()== ItemEvent.DESELECTED)
                {ui.hideTimeMenuItem.setSelected(false);ui.timerInd.setVisible(true);}
            }
        });

        ui.cmb1.addItemListener(e -> p1= (double)ui.cmb1.getSelectedItem());
        ui.cmb2.addItemListener(e -> p2= (double)ui.cmb2.getSelectedItem());
        ui.period1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int period;
                try
                {
                    period = Integer.parseInt(ui.period1.getText());
                    if (period > 0)
                    {t1=period;}
                    else throw new NumberFormatException();
                    }
                catch (NumberFormatException ex)
                {
                    t1=2;showError();
                }
                }
        });
        ui.period2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int period;
                try
                {
                    period = Integer.parseInt(ui.period2.getText());
                    if (period > 0 )
                    t2=period;
                    else throw new NumberFormatException();
                }
                catch (NumberFormatException ex)
                {
                    t2=3;showError();
                }
            }
        });
        ui.simulation_panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent code) {
                super.keyPressed(code);
                int key=code.getKeyCode();
                switch(key)
                {
                    case KeyEvent.VK_B:
                    {
                      if (simulation==false)
                        startSimulation(ui);
                    }break;
                    case KeyEvent.VK_E:
                    {
                        pauseSimulation(ui);
                        break;
                    }
                    case KeyEvent.VK_T:
                    {
                        if (!ui.timerInd.isVisible())
                        { ui.timerInd.setVisible(true);
                        ui.showTimeMenuItem.setSelected(false);
                        ui.showTimeButton.setSelected(false);
                        ui.hideTimeMenuItem.setSelected(true);
                        ui.hideTimeButton.setSelected(true);}
                        else
                            {
                        ui.timerInd.setVisible(false);
                        ui.showTimeMenuItem.setSelected(true);
                        ui.showTimeButton.setSelected(true);
                        ui.hideTimeMenuItem.setSelected(false);
                        ui.hideTimeButton.setSelected(false);
                        break; }

                    }
                }
            }
        });

    }
    private void pauseSimulation(UserInterface ui)
    {
        if (simulation==true)
        {
            timer.cancel();
            simulation = false;
            ui.stop_button.setEnabled(false);
            ui.start_button.setEnabled(true);
            if (ui.showDialogBox.isSelected())
            {
            showDialog(ui);
            }
        }
    }
    public void startSimulation(UserInterface ui)
    {
        if (simulation==false)
        {

            simulation=true;
            ui.start_button.setEnabled(false);
            ui.stop_button.setEnabled(true);
            timer = new Timer();
            TimerTask tt = new TimerTask()
            {
                @Override
                public void run() {
                    update(time,ui.simulation_panel);time=time+1000; ui.timerInd.setText("Время:" + time/1000 );
                }
            };
            timer.schedule(tt,0,1000);
        }
    }
    public void showError()
    {
        JDialog dialogError = new JDialog();
        dialogError.setTitle("Error");
        JTextArea errorInfo = new JTextArea();
        errorInfo.setFont(new Font("Helvetica",Font.BOLD,14));
        errorInfo.setText("Wrong number");
        errorInfo.setEditable(false);
        dialogError.add(errorInfo);
        dialogError.setVisible(true);
        errorInfo.setVisible(true);
        dialogError.setSize(new Dimension(30,75));
    }
    public void showDialog(UserInterface ui)
    {
        JPanel dialogPanel = new JPanel();
        JDialog dialog = new JDialog();
        dialog.setTitle("Info");
        JButton dialogCancel = new JButton("Cancel");
        JButton dialogOk = new JButton("Ok");
        JTextArea dialogText = new JTextArea();

        dialogText.setEditable(false);
        dialogText.setFont(new Font("TimesRoman", Font.ITALIC, 30));
        dialogText.setText("Всего домов: " + counter +
                "\nДеревянные: " + Wood.counter +
                "\nКаменные: " + Stone.counter +
                "\nВремя: " + (time/1000) + " секунд");

        dialogPanel.add(dialogText);
        dialogPanel.add(dialogOk);
        dialogPanel.add(dialogCancel);
        dialog.add(dialogPanel);
        dialog.setSize(new Dimension(300, 250));


        dialog.setResizable(false);
        dialog.setVisible(true);

        dialogOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button pressed");
                dialog.dispose();
                time = 0;
                counter = 0;
                Wood.counter = 0;
                Stone.counter = 0;
                ui.timerInd.setText("Время:" + time/1000 );
                ui.frame.repaint();
            }
        });
        dialogCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.stop_button.setEnabled(true);
                ui.start_button.setEnabled(false);
                startSimulation(ui);
                dialog.dispose();
            }
        });
    }
    public void update (float time, JPanel simulation_panel)
    {
        System.out.println(p1);
        System.out.println(t1);

        if (time/1000 % t1 == 0 && time!=0 )
        {
            if (Math.random() < p1)
            {
                array.add(allFactory.createStone((int)(Math.random()*simulation_panel.getWidth()),(int)(Math.random()*simulation_panel.getHeight())),counter);
                simulation_panel.getGraphics().drawImage(array.getHouse(counter).getImage(),
                        array.getHouse(counter).getX(), array.getHouse(counter).getY(), null);
                counter++;
            }
        }
        if (time/1000 % t2 == 0 && time!=0 ) {
            if (Math.random() < p2) {
                array.add(allFactory.createWood((int) (Math.random() * simulation_panel.getWidth()), (int) (Math.random() * simulation_panel.getHeight())),counter);
                simulation_panel.getGraphics().drawImage(array.getHouse(counter).getImage(), array.getHouse(counter).getX(), array.getHouse(counter).getY(), null);
                counter++;
            }
        }
    }
}
