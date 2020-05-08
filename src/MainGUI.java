import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;


public class MainGUI {
    private Timer timer;
    private JPanel mainPanel;
    private Habitat action;
    private JMenuBar mainMenu;
    private JMenuItem exitMenuItem;
    private JMenuItem startMenuItem;
    private JMenuItem stopMenuItem;
    private JCheckBox checkBox;
    private JPanel statsPanel;
    private JPanel controlPanel;
    //private JComboBox<Integer> comboBox; //combobox related
    private JSlider maleSlider;
    private JSlider workerSlider;
    private JSlider kSlider;
    private JRadioButton radioButtonYes;
    private JRadioButton radioButtonNo;
    private JTextField malePeriodField;
    private JTextField workerPeriodField;
    private JButton startButton;
    private JButton stopButton;
    private JTextField timerCount;
    private JLabel timerLabel;
    private boolean ACCESS_TO_PRESS_B = true;
    private boolean ACCESS_TO_PRESS_P = true;
    private boolean ACCESS_TO_PRESS_T = false;
    private boolean ACCESS_TO_SHOW_STATS = false;

    public MainGUI(String name, int frameHeight, int frameWidth) {
        JFrame mainFrame = new JFrame(name);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/favicon.png")));
        mainFrame.setBounds((dimension.width - frameWidth) / 2, (dimension.height - frameHeight) / 2, frameWidth, frameHeight);
        mainFrame.setBackground(new Color(233, 217, 215));
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setResizable(false);
        controlPanel = initControlPanel("Control Panel");
        controlPanel.setPreferredSize(new Dimension(400, frameHeight));
        mainMenu = initMenu();
        mainMenu.setPreferredSize(new Dimension(0, 25));
        stopMenuItem.setEnabled(false);
        action = new Habitat(frameHeight - mainMenu.getPreferredSize().height, frameWidth - controlPanel.getPreferredSize().width);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(233, 217, 215));
        mainPanel.add(controlPanel, BorderLayout.EAST);
        mainPanel.add(action, BorderLayout.CENTER);
        mainFrame.add(mainPanel);
        mainFrame.setJMenuBar(mainMenu);
        mainPanel.addKeyListener(new KeyLogger());
        addButtonListeners();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainPanel.requestFocus();
    }



    private JMenuBar initMenu() {
        JMenuBar mainMenu = new JMenuBar();
        JMenu appMenuButton = new JMenu("Application");
        exitMenuItem = new JMenuItem("Exit");
        startMenuItem = new JMenuItem("Start", 'S');
        stopMenuItem = new JMenuItem("Stop", 'S');
        startMenuItem.setAccelerator(KeyStroke.getKeyStroke("B"));
        stopMenuItem.setAccelerator(KeyStroke.getKeyStroke("P"));
        appMenuButton.add(startMenuItem);
        appMenuButton.add(stopMenuItem);
        appMenuButton.addSeparator();
        appMenuButton.add(exitMenuItem);
        mainMenu.add(appMenuButton);
        return mainMenu;
    }

    private JPanel initControlPanel(String title) {
        JPanel controlPanel = new JPanel();
        JPanel header;
        JPanel footer;
        JPanel aside;
        JPanel section;
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBackground(new Color(0, 90, 233, 185));
        header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleOfPanel = new JLabel("Control Panel");
        header.setPreferredSize(new Dimension(150, 100));
        Font font = new Font("Times New Roman", Font.ITALIC, 30);
        titleOfPanel.setFont(font);
        header.add(titleOfPanel);
        section = new JPanel(new GridLayout(1, 0, 0, 0));
        JPanel section1 = new JPanel(new BorderLayout());
        JPanel sub_section1 = new JPanel(new GridLayout(7, 0, 0, 2));
        sub_section1.setBackground(new Color(0, 90, 233, 185));
        section1.setBackground(Color.red);
        section1.add(sub_section1, BorderLayout.CENTER);
        section.add(section1);

        JPanel radioFloor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel radioBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel radioLabel = new JLabel("Show Time");
        ButtonGroup buttonGroup = new ButtonGroup();
        radioButtonYes = new JRadioButton("YES");
        radioButtonNo = new JRadioButton("NO");
        radioButtonYes.setFocusable(false);
        radioButtonNo.setFocusable(false);
        buttonGroup.add(radioButtonYes);
        buttonGroup.add(radioButtonNo);
        radioFloor.add(radioLabel);
        radioBox.add(radioButtonYes);
        radioBox.add(radioButtonNo);
        radioFloor.add(radioBox);

        JPanel checkboxFloor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel chekboxLabel = new JLabel("Show info");
        checkBox = new JCheckBox();
        checkBox.setFocusable(false);
        checkboxFloor.add(chekboxLabel);
        checkboxFloor.add(checkBox);

        JPanel setPeriodL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spawnLCarLabel = new JLabel("Male period");
        malePeriodField = new JTextField(3);
        setPeriodL.add(spawnLCarLabel);
        setPeriodL.add(malePeriodField);

        JPanel setPeriodH = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spawnHCarLabel = new JLabel("Worker period");
        workerPeriodField = new JTextField(3);
        setPeriodH.add(spawnHCarLabel);
        setPeriodH.add(workerPeriodField);

        sub_section1.add(radioFloor);
        sub_section1.add(checkboxFloor);
        sub_section1.add(setPeriodL);
        sub_section1.add(setPeriodH);

                        //combobox related
        /*JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel comboBoxLabel = new JLabel("Male's ");
        comboBox = new JComboBox<Integer>();
        for (int i = 0; i <= 10; i++) comboBox.addItem(i * 10);
        comboBox.setFocusable(false);
        comboBoxPanel.add(comboBoxLabel);
        comboBoxPanel.add(comboBox);*/


        JPanel maleSliderPanel = new JPanel(new GridLayout(2, 0, 0, 0));
        JPanel subMaleSliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel maleSliderLabel = new JLabel("Male's chance");
        maleSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        maleSlider.setFocusable(false);
        maleSlider.setMajorTickSpacing(10);
        maleSlider.setPaintTicks(true);
        maleSlider.setSnapToTicks(true);
        maleSlider.setPaintLabels(true);
        subMaleSliderPanel.add(maleSliderLabel);
        maleSliderPanel.add(subMaleSliderPanel);
        maleSliderPanel.add(maleSlider);

        JPanel workerSliderPanel = new JPanel(new GridLayout(2, 0, 0, 0));
        JPanel subWorkerSliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel workerSliderLabel = new JLabel("Worker's chance");
        workerSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        workerSlider.setFocusable(false);
        workerSlider.setMajorTickSpacing(10);
        workerSlider.setPaintTicks(true);
        workerSlider.setSnapToTicks(true);
        workerSlider.setPaintLabels(true);
        subWorkerSliderPanel.add(workerSliderLabel);
        workerSliderPanel.add(subWorkerSliderPanel);
        workerSliderPanel.add(workerSlider);


        JPanel kSliderPanel = new JPanel(new GridLayout(2, 0, 0, 0));
        JPanel subKSliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel kSliderLabel = new JLabel("K percent");
        kSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        kSlider.setFocusable(false);
        kSlider.setMajorTickSpacing(10);
        kSlider.setPaintTicks(true);
        kSlider.setSnapToTicks(true);
        kSlider.setPaintLabels(true);
        subKSliderPanel.add(kSliderLabel);
        kSliderPanel.add(subKSliderPanel);
        kSliderPanel.add(kSlider);

        sub_section1.add(maleSliderPanel);
        sub_section1.add(workerSliderPanel);
        sub_section1.add(kSliderPanel);

        //sub_section1.add(comboBoxPanel); //combobox related
        aside = new JPanel(new BorderLayout());
        JPanel timerFloat;
        timerFloat = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Font font2 = new Font("Arial", Font.PLAIN, 40);
        Font font3 = new Font("Arial", Font.PLAIN, 30);
        aside.setBackground(new Color(233, 0, 25));
        aside.setPreferredSize(new Dimension(150, 0));
        timerCount = new JTextField(3);
        timerLabel = new JLabel();
        timerCount.setFont(font3);
        timerLabel.setText("TIME");
        timerLabel.setFont(font2);
        timerCount.setEnabled(false);
        timerCount.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        timerCount.setDisabledTextColor(Color.black);
        timerCount.setBackground(new Color(0, 90, 233, 255));
        timerFloat.add(timerLabel);
        timerFloat.add(timerCount);
        timerFloat.setBackground(new Color(0, 90, 233, 255));
        showTime(false);
        aside.add(timerFloat, BorderLayout.CENTER);

        footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        startButton.setFocusable(false);
        stopButton.setFocusable(false);
        stopButton.setEnabled(false);
        footer.setBackground(new Color(106, 73, 233));
        footer.add(startButton);
        footer.add(stopButton);

        controlPanel.add(aside, BorderLayout.EAST);
        controlPanel.add(footer, BorderLayout.SOUTH);
        controlPanel.add(header, BorderLayout.NORTH);
        controlPanel.add(section, BorderLayout.CENTER);
        return controlPanel;
    }

    private JDialog initModalDialog(String title) {
        JDialog modalPane = new JDialog();
        JPanel buttonBox = new JPanel();
        JTextArea textArea = new JTextArea();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(e -> {
            modalPane.setVisible(false);
            action.killTransportList();
            action.setVisible(false);
            controlPanel.setVisible(false);
            mainMenu.setVisible(false);
            statsPanel = initStatisticsPanel();
            mainPanel.add(statsPanel);
            statsPanel.requestFocusInWindow();
        });
        cancelButton.addActionListener(e -> {
            modalPane.setVisible(false);
        });
        Font font = new Font("Times New Roman", Font.BOLD, 20);
        modalPane.setModal(true);
        modalPane.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/favicon.png")));
        modalPane.setResizable(false);
        modalPane.setTitle(title);
        modalPane.setBounds(500, 500, 500, 500);
        textArea.setDisabledTextColor(Color.BLACK);
        textArea.setFont(font);
        textArea.append("Time: " + timerCount.getText() + "\n\n");
        textArea.append("Bees Generated: " + (action.getBeeMaleAmount() + action.getBeeWorkerAmount()) + "\n\n");
        textArea.append("Male bees: " + action.getBeeMaleAmount() + "\n\n");
        textArea.append("Worker bees: " + action.getBeeWorkerAmount() + "\n\n");
        textArea.setLineWrap(false);
        textArea.setEnabled(false);
        textArea.setBackground(Color.WHITE);
        buttonBox.add(okButton);
        buttonBox.add(cancelButton);
        modalPane.add(buttonBox, BorderLayout.SOUTH);
        modalPane.add(textArea, BorderLayout.CENTER);
        return modalPane;
    }

    private void addButtonListeners() {
        startButton.addActionListener(e -> {
            if (ACCESS_TO_PRESS_B) {
                startTimer();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                ACCESS_TO_PRESS_B = false;
                ACCESS_TO_PRESS_P = true;
                startMenuItem.setEnabled(false);
                stopMenuItem.setEnabled(true);
            }
        });
        stopButton.addActionListener(e -> {
            if (!ACCESS_TO_PRESS_B && ACCESS_TO_PRESS_P) {
                stopTimer();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                ACCESS_TO_PRESS_P = false;
                ACCESS_TO_PRESS_B = true;
                startMenuItem.setEnabled(true);
                stopMenuItem.setEnabled(false);
                if (ACCESS_TO_SHOW_STATS) {
                    JDialog modalPane = initModalDialog("Statistics");
                    modalPane.setVisible(true);
                }
            }
        });
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ACCESS_TO_SHOW_STATS = !ACCESS_TO_SHOW_STATS;
            }
        });
        radioButtonYes.addActionListener(e -> {
            showTime(true);
        });
        radioButtonNo.addActionListener(e -> {
            showTime(false);
        });
        malePeriodField.addActionListener(e -> {
            try {
                if (Integer.parseInt(malePeriodField.getText()) <= 0) {
                    if (ACCESS_TO_PRESS_P && !ACCESS_TO_PRESS_B) stopTimer();
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    ACCESS_TO_PRESS_P = false;
                    ACCESS_TO_PRESS_B = true;
                    startMenuItem.setEnabled(true);
                    stopMenuItem.setEnabled(false);
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setMaleTimePeriod(action.getMaleDefaultPeriod());
                    malePeriodField.setText("");
                    mainPanel.requestFocus();
                    return;
                }
                action.setMaleTimePeriod(Integer.parseInt(malePeriodField.getText()));
            } catch (NumberFormatException | ArithmeticException ex) {
                if (action.getCountOfTicks() != 0 && !ACCESS_TO_PRESS_P && ACCESS_TO_PRESS_B) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setMaleTimePeriod(action.getMaleDefaultPeriod());

                } else if (action.getCountOfTicks() != 0 && ACCESS_TO_PRESS_P && !ACCESS_TO_PRESS_B) {
                    stopTimer();
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setMaleTimePeriod(action.getMaleDefaultPeriod());
                    startTimer();
                } else if (action.getCountOfTicks() == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setMaleTimePeriod(action.getMaleDefaultPeriod());
                }
                malePeriodField.setText("");
            }
            mainPanel.requestFocus();
        });
        workerPeriodField.addActionListener(e -> {
            try {
                if (Integer.parseInt(workerPeriodField.getText()) <= 0) {
                    if (ACCESS_TO_PRESS_P && !ACCESS_TO_PRESS_B) stopTimer();
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    ACCESS_TO_PRESS_P = false;
                    ACCESS_TO_PRESS_B = true;
                    startMenuItem.setEnabled(true);
                    stopMenuItem.setEnabled(false);
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setWorkerTimePeriod(action.getWorkerDefaultPeriod());
                    workerPeriodField.setText("");
                    mainPanel.requestFocus();
                    return;
                }
                action.setWorkerTimePeriod(Integer.parseInt(workerPeriodField.getText()));
            } catch (NumberFormatException | ArithmeticException ex) {
                if (action.getCountOfTicks() != 0 && !ACCESS_TO_PRESS_P && ACCESS_TO_PRESS_B) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setWorkerTimePeriod(action.getWorkerDefaultPeriod());
                } else if (action.getCountOfTicks() != 0 && ACCESS_TO_PRESS_P && !ACCESS_TO_PRESS_B) {
                    stopTimer();
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setWorkerTimePeriod(action.getWorkerDefaultPeriod());
                    startTimer();
                } else if (action.getCountOfTicks() == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                    action.setWorkerTimePeriod(action.getWorkerDefaultPeriod());
                }
                workerPeriodField.setText("");
            }
            mainPanel.requestFocus();
        });
        workerSlider.addChangeListener(e -> {
            action.setWorkerChance(workerSlider.getValue());
        });
        maleSlider.addChangeListener(e -> {
            action.setMaleChance(maleSlider.getValue());
        });
        kSlider.addChangeListener(e -> {
            action.setKPercent(kSlider.getValue());
        });
        startMenuItem.addActionListener(e -> {
            if (ACCESS_TO_PRESS_B) {
                startTimer();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                ACCESS_TO_PRESS_B = false;
                ACCESS_TO_PRESS_P = true;
                startMenuItem.setEnabled(false);
                stopMenuItem.setEnabled(true);
            }
        });
        stopMenuItem.addActionListener(e -> {
            if (!ACCESS_TO_PRESS_B && ACCESS_TO_PRESS_P) {
                stopTimer();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                ACCESS_TO_PRESS_P = false;
                ACCESS_TO_PRESS_B = true;
                startMenuItem.setEnabled(true);
                stopMenuItem.setEnabled(false);
                if (ACCESS_TO_SHOW_STATS) {
                    JDialog modalPane = initModalDialog("Statistics");
                    modalPane.setVisible(true);
                }
            }
        });
        exitMenuItem.addActionListener(e -> {
            System.exit(0);
        });
    }

    private JPanel initStatisticsPanel() {
        Font font = new Font("Arial", Font.CENTER_BASELINE, 18);
        JPanel statsPanel = new JPanel(new GridLayout(4, 0, 0, -100));
        JLabel time = new JLabel("Time: " + timerCount.getText());
        JLabel count = new JLabel("Bees Generated: " + (action.getBeeMaleAmount() + action.getBeeWorkerAmount()));
        JLabel countingMaleBees = new JLabel("Male bees amount: " + action.getBeeMaleAmount());
        JLabel countingWorkerBees = new JLabel("Worker bees amount: " + action.getBeeWorkerAmount());
        time.setFont(font);
        count.setFont(font);
        countingMaleBees.setFont(font);
        countingWorkerBees.setFont(font);
        statsPanel.add(time);
        statsPanel.add(count);
        statsPanel.add(countingMaleBees);
        statsPanel.add(countingWorkerBees);
        statsPanel.setVisible(true);
        return statsPanel;
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                action.update();
                timerCount.setText(String.valueOf(action.getCountOfTicks()));
            }
        }, 0, 1000);
    }

    private void showTime(boolean flag) {
        timerLabel.setVisible(flag);
        timerCount.setVisible(flag);
    }

    private void stopTimer() {
        timer.cancel();
    }

    private class KeyLogger extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyChar()) {
                case KeyEvent.VK_B: {
                    if (ACCESS_TO_PRESS_B) {
                        startTimer();
                        startButton.setEnabled(false);
                        stopButton.setEnabled(true);
                        ACCESS_TO_PRESS_B = false;
                        ACCESS_TO_PRESS_P = true;
                        startMenuItem.setEnabled(false);
                        stopMenuItem.setEnabled(true);
                    }
                }
                break;
                case KeyEvent.VK_P: {
                    if (!ACCESS_TO_PRESS_B && ACCESS_TO_PRESS_P) {
                        stopTimer();
                        startButton.setEnabled(true);
                        stopButton.setEnabled(false);
                        ACCESS_TO_PRESS_P = false;
                        ACCESS_TO_PRESS_B = true;
                        startMenuItem.setEnabled(true);
                        stopMenuItem.setEnabled(false);
                        if (ACCESS_TO_SHOW_STATS) {
                            JDialog modalPane = initModalDialog("Statistics");
                            modalPane.setVisible(true);
                        }
                    }
                }
                break;
                case KeyEvent.VK_T: {
                    ACCESS_TO_PRESS_T = !ACCESS_TO_PRESS_T;
                    showTime(ACCESS_TO_PRESS_T);
                    radioButtonYes.setSelected(ACCESS_TO_PRESS_T);
                    radioButtonNo.setSelected(!ACCESS_TO_PRESS_T);
                }
                break;
                case KeyEvent.VK_E: {
                    if (action.getCountOfTicks() == 0) return;
                    stopTimer();
                    action.killTransportList();
                    action.setVisible(false);
                    controlPanel.setVisible(false);
                    mainMenu.setVisible(false);
                    statsPanel = initStatisticsPanel();
                    mainPanel.add(statsPanel);
                    statsPanel.requestFocus();
                }
                break;
            }
        }
    }
}