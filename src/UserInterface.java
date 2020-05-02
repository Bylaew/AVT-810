import javax.swing.*;
import java.awt.*;

public class UserInterface
{
    public JFrame frame = new JFrame("Simulation");
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
    JButton aliveButton = new JButton("Show alive");

    ButtonGroup stoneAi = new ButtonGroup();
    ButtonGroup woodAi = new ButtonGroup();
    JToggleButton sleepStoneAi = new JToggleButton("StoneAi sleep");
    JToggleButton sleepWoodAi = new JToggleButton("WoodAi sleep");
    JToggleButton wakeStoneAi = new JToggleButton("StoneAi wake");
    JToggleButton wakeWoodAi = new JToggleButton("WoodAi wake");
    //чекбокс
    JCheckBox showDialogBox = new JCheckBox("Show dialog");
    //связанные переключатели
    JPanel timeSwitchPanel = new JPanel();
    ButtonGroup bg= new ButtonGroup();
    JRadioButton showTimeButton= new JRadioButton("Show time");
    JRadioButton hideTimeButton= new JRadioButton("Hide time");
    JPanel woodAi_panel = new JPanel();
    JPanel stoneAi_panel = new JPanel();

    //комбобокс
    JComboBox cmb1 = new JComboBox();
    JComboBox cmb2 = new JComboBox();
    JComboBox cmbPriority1 = new JComboBox();
    JComboBox cmbPriority2 = new JComboBox();

    JPanel priority_panel = new JPanel();

    //текстовые зоны
    JTextField period1 = new JTextField();
    JTextField period2 = new JTextField();
    JTextField stoneLifeTime = new JTextField();
    JTextField woodLifeTime = new JTextField();

    UserInterface(){
        frame.add(settings_panel, BorderLayout.WEST);// настройки слева
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

        stoneAi.add(sleepStoneAi);stoneAi.add(wakeStoneAi);
        woodAi.add(sleepWoodAi);woodAi.add(wakeWoodAi);
        //настройки комобоксов
        cmb1.setEditable(false);
        cmb2.setEditable(false);
        cmbPriority1.setEditable(false);
        cmbPriority2.setEditable(false);

        for (int i = 1; i < 10;i=i+1 )
        {
            cmb1.addItem((double)i/10);
            cmb2.addItem((double)i/10);
            cmbPriority1.addItem(i);
            cmbPriority2.addItem(i);
        }
        //добавление элементов к панели настроек
        settings_panel.setLayout(new GridLayout(20,1,0,0));
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
        settings_panel.add(new JLabel("Stone life"));
        settings_panel.add(stoneLifeTime);
        settings_panel.add(new JLabel("Wood life"));
        settings_panel.add(woodLifeTime);

        settings_panel.add(aliveButton);
        settings_panel.add(stoneAi_panel);
        settings_panel.add(woodAi_panel);

        stoneAi_panel.add(sleepStoneAi);
        stoneAi_panel.add(wakeStoneAi);
        woodAi_panel.add(sleepWoodAi);
        woodAi_panel.add(wakeWoodAi);
        settings_panel.add(priority_panel);

        priority_panel.add(cmbPriority1);
        priority_panel.add(cmbPriority2);
        settings_panel.add(priority_panel);

        simulation_panel.setVisible(true);
        settings_panel.setVisible(true);};

}
