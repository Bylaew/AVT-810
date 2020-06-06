package habitat;

import ai.*;
import factory.*;
import objects.*;
import singleton.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Timer;


public class Habitat {

    int width;
    int height;
    private ConcreteFactory allFactory;
    boolean simulation = false;
    private Timer timer;
    public float time;
    public  Singleton houseSingleton = Singleton.getInstance();
    private int counter;
    public UserInterface ui;
    //условия: вероятности и время
    private float t1, t2;
    private double p1, p2;
    private int stoneLifeTime;
    private int woodLifeTime;
    public ClientConnection connection;
    //AI
    WoodAI woodAI = null;
    StoneAI stoneAI = null;
    //DataBase
    HouseDB houseDB;

    public Habitat() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        houseDB = new HouseDB();
        ui = new UserInterface();
        this.height = 720;
        this.width = 1280;
        allFactory = new ConcreteFactory();
        ui.frame.setSize(width, height);
        ui.frame.setVisible(true);

        ui.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        if (!new File("config.txt").exists()){ t1 = 2;t2 = 3;p1 = 0.8;p2 = 0.7;stoneLifeTime = 10000;woodLifeTime = 10000; }
        else {
            try {
                loadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ui.cmb1.setSelectedItem(p1);
        ui.cmb2.setSelectedItem(p2);
        ui.stoneLifeTime.setText(String.valueOf(stoneLifeTime));
        ui.woodLifeTime.setText(String.valueOf(woodLifeTime));
        ui.period1.setText(String.valueOf(t1));
        ui.period2.setText(String.valueOf(t2));

        counter = 0;
        time = 0;
        connection = new ClientConnection(this);

        //слушатели
        ui.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    saveConfig();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        ui.simulation_panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ui.simulation_panel.requestFocus();

            }
        });
        ui.aliveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAlive(ui);
            }

        });
        ui.start_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation(ui);
            }

        });
        ui.startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ui.showDialogBox.setSelected(true);
                } else {
                    ui.showDialogBox.setSelected(false);
                }
            }
        });
        ui.showDialogBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ui.showDialogMenuItem.setSelected(true);
                } else {
                    ui.showDialogMenuItem.setSelected(false);
                }
            }
        });

        ui.showTimeMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ui.showTimeButton.setSelected(true);
                    ui.timerInd.setVisible(true);
                }
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    ui.showTimeButton.setSelected(false);
                    ui.timerInd.setVisible(false);
                }
            }
        });
        ui.showTimeButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ui.showTimeMenuItem.setSelected(true);
                    ui.timerInd.setVisible(true);
                }
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    ui.showTimeMenuItem.setSelected(false);
                    ui.timerInd.setVisible(false);
                }
            }
        });

        ui.hideTimeMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ui.hideTimeButton.setSelected(true);
                    ui.timerInd.setVisible(false);
                }
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    ui.hideTimeButton.setSelected(false);
                    ui.timerInd.setVisible(true);
                }
            }
        });
        ui.hideTimeButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ui.hideTimeMenuItem.setSelected(true);
                    ui.timerInd.setVisible(false);
                }
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    ui.hideTimeMenuItem.setSelected(false);
                    ui.timerInd.setVisible(true);
                }
            }
        });

        ui.cmb1.addItemListener(e -> {
            p1 = (double) ui.cmb1.getSelectedItem();
        });
        ui.cmb2.addItemListener(e -> p2 = (double) ui.cmb2.getSelectedItem());
        ui.cmbPriority1.addItemListener(e -> {
            stoneAI.setPriority((int) ui.cmbPriority1.getSelectedItem());
        });
        ui.cmbPriority2.addItemListener(e -> {
            woodAI.setPriority((int) ui.cmbPriority2.getSelectedItem());
        });

        ui.period1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int period;
                try {
                    period = Integer.parseInt(ui.period1.getText());
                    if (period > 0) {
                        t1 = period;
                    } else throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    t1 = 2;
                    showError();
                }
            }
        });
        ui.period2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int period;
                try {
                    period = Integer.parseInt(ui.period2.getText());
                    if (period > 0)
                        t2 = period;
                    else throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    t2 = 3;
                    showError();
                }
            }
        });
        ui.stoneLifeTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int lifeTime;
                try {
                    lifeTime = Integer.parseInt(ui.stoneLifeTime.getText());
                    if (lifeTime > 0)
                        stoneLifeTime = lifeTime;
                    else throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    stoneLifeTime = 5;
                    showError();
                }
            }
        });
        ui.woodLifeTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int lifeTime;
                try {
                    lifeTime = Integer.parseInt(ui.woodLifeTime.getText());
                    if (lifeTime > 0)
                        woodLifeTime = lifeTime;
                    else throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    woodLifeTime = 5;
                    showError();
                }
            }
        });

        ui.sleepStoneAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stoneAI.pauseAI();
            }
        });
        ui.sleepWoodAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                woodAI.pauseAI();
            }
        });
        ui.wakeWoodAi.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                woodAI.resumeAI();
            }
        });
        ui.wakeStoneAi.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                synchronized (stoneAI) {
                    stoneAI.resumeAI();
                }
            }
        });


        ui.simulation_panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent code) {
                super.keyPressed(code);
                int key = code.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_B: {
                        if (simulation == false)
                            startSimulation(ui);
                    }
                    break;
                    case KeyEvent.VK_E: {
                        pauseSimulation(ui);
                        break;
                    }
                    case KeyEvent.VK_T: {
                        if (!ui.timerInd.isVisible()) {
                            ui.timerInd.setVisible(true);
                            ui.showTimeMenuItem.setSelected(false);
                            ui.showTimeButton.setSelected(false);
                            ui.hideTimeMenuItem.setSelected(true);
                            ui.hideTimeButton.setSelected(true);
                        } else {
                            ui.timerInd.setVisible(false);
                            ui.showTimeMenuItem.setSelected(true);
                            ui.showTimeButton.setSelected(true);
                            ui.hideTimeMenuItem.setSelected(false);
                            ui.hideTimeButton.setSelected(false);
                            break;
                        }

                    }
                }
            }
        });
        ui.saveObjButton.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                try {
                    houseSingleton.saveObj();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        ui.loadObjButton.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                try {
                    loadObj();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ui.loadConfigButton.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                try {
                    loadConfig();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        ui.saveConfigButton.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                try {
                    saveConfig();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ui.consoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConsole();
            }

        });

        ui.saveAllItem.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                houseDB.insertAll(houseSingleton.houseList);
            }
        });
        ui.loadAllItem.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                Vector<House> newVector = houseDB.loadWood();
                for (int i=0; i<newVector.size();i++)
                    houseSingleton.houseList.add(newVector.get(i));
            }
        });
        ui.saveWoodItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<House> newHouseVector= new Vector<>();
                for (int i=0; i<houseSingleton.houseList.size();i++)
                    if (houseSingleton.houseList.get(i) instanceof Wood)
                        newHouseVector.add(houseSingleton.houseList.get(i));
                    houseDB.insertWood(newHouseVector);
            }
        });
        ui.loadWoodItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            Vector<House> newWoodVector = houseDB.loadWood();
            for (int i=0; i<newWoodVector.size();i++)
                houseSingleton.houseList.add(newWoodVector.get(i));
            }
        });
        ui.saveStoneItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<House> newHouseVector= new Vector<>();
                for (int i=0; i<houseSingleton.houseList.size();i++)
                    if (houseSingleton.houseList.get(i) instanceof Stone)
                        newHouseVector.add(houseSingleton.houseList.get(i));
                    houseDB.insertStone(newHouseVector);
            }
        });
        ui.loadStoneItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<House> newStoneVector = houseDB.loadStone();
                for (int i=0; i<newStoneVector.size();i++)
                    houseSingleton.houseList.add(newStoneVector.get(i));
            }

        });

    }

    public void pauseSimulation(UserInterface ui) {
        if (simulation == true) {
            timer.cancel();
            simulation = false;
            ui.stop_button.setEnabled(false);
            ui.start_button.setEnabled(true);
            if (ui.showDialogBox.isSelected()) {
                showDialog(ui);
            }
            houseSingleton.clear();
        }
    }

    public void startSimulation(UserInterface ui) {
        if (simulation == false) {

            simulation = true;
            ui.start_button.setEnabled(false);
            ui.stop_button.setEnabled(true);
            timer = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    update(time, ui.simulation_panel);
                    time = time + 20;
                    ui.timerInd.setText("Время:" + time / 1000);
                    draw();
                }
            };
            timer.schedule(tt, 0, 20);
            woodAI = new WoodAI(this);
            stoneAI = new StoneAI(this);

            woodAI.start();
            woodAI.working = true;
            ui.wakeStoneAi.setSelected(true);
            ui.wakeWoodAi.setSelected(true);
            ui.showTimeButton.setSelected(true);

            stoneAI.start();
            stoneAI.working = true;

        }
    }

    public void showError() {
        JDialog dialogError = new JDialog();
        dialogError.setTitle("Error");
        JTextArea errorInfo = new JTextArea();
        errorInfo.setFont(new Font("Helvetica", Font.BOLD, 14));
        errorInfo.setText("Wrong number");
        errorInfo.setEditable(false);
        dialogError.add(errorInfo);
        dialogError.setVisible(true);
        errorInfo.setVisible(true);
        dialogError.setSize(new Dimension(30, 75));
    }

    public void showDialog(UserInterface ui) {
        JPanel dialogPanel = new JPanel();
        JDialog dialog = new JDialog();
        dialog.setTitle("Info");
        JButton dialogCancel = new JButton("Cancel");
        JButton dialogOk = new JButton("Ok");
        JTextArea dialogText = new JTextArea();

        dialogText.setEditable(false);
        dialogText.setFont(new Font("TimesRoman", Font.ITALIC, 30));
        dialogText.setText("Всего домов: " + houseSingleton.houseList.size() +
                "\nДеревянные: " + Wood.counter +
                "\nКаменные: " + Stone.counter +
                "\nВремя: " + (time / 1000) + " секунд");

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
                ui.timerInd.setText("Время:" + time / 1000);
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

    public void showConsole() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Console");
        JTextArea console = new JTextArea();
        dialog.add(console);
        console.setEditable(true);
        dialog.setSize(new Dimension(600, 400));
        dialog.setVisible(true);
        console.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == e.VK_ENTER) {
                    String[] input = console.getText().split("\n");

                    if(input[input.length-1].equals("getchance")) {
                        console.append("\n" + p1 + "\n");
                        System.out.println(input[input.length-1].split(" "));
                    }
                    else {
                        String[] newInput;
                        String com = "";double temp = 0;double temp2;
                        newInput=input[input.length-1].split(" ");
                        if (newInput.length==2) {
                            if (newInput[0].equals("setchance")) {
                                p1=Double.parseDouble(newInput[1]);
                                ui.cmb1.setSelectedItem(p1);
                            }
                            if (newInput[0].equals("get")) {
                                int id = Integer.parseInt(newInput[1]);
                                try {
                                    connection.receive(id);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                    console.setCaretPosition(console.getText().length());
                }

            }
        });

    }

    public void showAlive(UserInterface ui) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Alive");
        String[] cols = {"ID", "LifeTime"};
        int rows = 100;
        JTable table = new JTable(new DefaultTableModel(cols, rows));
        TableModel tableModel = table.getModel();

        int i = 0;

        for (Iterator it = houseSingleton.mapList.descendingMap().entrySet().iterator(); it.hasNext(); i++) {
            Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) it.next();
            tableModel.setValueAt(entry.getValue(), i, 1);
            tableModel.setValueAt(entry.getKey(), i, 0);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane);
        dialog.setSize(new Dimension(250, 100));
        dialog.setResizable(false);
        dialog.setVisible(true);
        dialog.pack();
    }

    public void update(float time, JPanel simulation_panel) {
        if (time / 1000 % t1 == 0 && time != 0) {
            if (Math.random() < p1) {
                int id = houseSingleton.idRandom();
                House stoneHouse = allFactory.createStone((int) (Math.random() * simulation_panel.getWidth()), (int) (Math.random() * simulation_panel.getHeight()));
                stoneHouse.setAppTime((int) time);
                stoneHouse.setLifePeriod(stoneLifeTime);
                stoneHouse.setId(id);
                houseSingleton.houseList.add(houseSingleton.houseList.size(), stoneHouse);
                houseSingleton.mapList.put(id, (long) stoneHouse.appTime);
                houseSingleton.idList.add(id);
                counter++;
            }
        }
        if (time / 1000 % t2 == 0 && time != 0) {
            if (Math.random() < p2) {
                int id = houseSingleton.idRandom();
                House woodHouse = allFactory.createWood((int) (Math.random() * simulation_panel.getWidth()), (int) (Math.random() * simulation_panel.getHeight()));
                woodHouse.setAppTime((int) time);
                woodHouse.setLifePeriod(stoneLifeTime);
                woodHouse.setId(id);
                houseSingleton.houseList.add(houseSingleton.houseList.size(), woodHouse);
                houseSingleton.mapList.put(id, (long) woodHouse.appTime);
                houseSingleton.idList.add(id);
                counter++;
            }
        }
        for (int i = 0; i < houseSingleton.houseList.size() - 1; i++) {
            if (houseSingleton.houseList.get(i).appTime + houseSingleton.houseList.get(i).lifePeriod <= time) {
                houseSingleton.mapList.remove(houseSingleton.houseList.get(i).id);
                houseSingleton.idList.remove(houseSingleton.houseList.get(i).id);
                houseSingleton.houseList.remove(i);
                counter--;
            }
        }
    }

    public void draw() {
        int w = ui.simulation_panel.getWidth();
        int h = ui.simulation_panel.getHeight();
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < houseSingleton.houseList.size() ; i++) {
            bufferedImage.getGraphics().drawImage(houseSingleton.houseList.get(i).getImage(),
                    (int) houseSingleton.houseList.get(i).getX(), (int) houseSingleton.houseList.get(i).getY(), null);
        }

        ui.simulation_panel.getGraphics().drawImage(bufferedImage, 0, 0, w, h, null);

    }

    public void loadObj() throws IOException, ClassNotFoundException {
        FileInputStream fileInput = new FileInputStream("Obj.out");
        ObjectInputStream objIn = new ObjectInputStream(fileInput);
        this.houseSingleton = (Singleton) objIn.readObject();
    }

    public void saveConfig() throws IOException {
        FileWriter nFile = new FileWriter("config.txt");
        nFile.write(t1 +"\n" );
        nFile.write(t2 +"\n" );
        nFile.write(p1 +"\n" );
        nFile.write(p2 +"\n" );
        nFile.write( stoneLifeTime +"\n" );
        nFile.write( woodLifeTime +"\n" );
        nFile.close();
    }

    public void loadConfig() throws IOException {
        FileReader fr = new FileReader("config.txt");
        Scanner scan = new Scanner(fr);

        if (scan.hasNextLine()) {

            this.t1 = Float.parseFloat(scan.nextLine());
            this.t2 = Float.parseFloat(scan.nextLine());
            this.p1 = Double.parseDouble(scan.nextLine());
            this.p2 = Double.parseDouble(scan.nextLine());
            this.stoneLifeTime = Integer.parseInt(scan.nextLine());
            this.woodLifeTime = Integer.parseInt(scan.nextLine());
            fr.close();
        }
    }

    public void updateConnections(ArrayList<Integer> list)
    {

       System.out.println("Updating connection list");
        ui.connectionField.setText("Current connections:");
      for (int i = 0; i<list.size();i++)
        ui.connectionField.setText(ui.connectionField.getText()+"\n"+list.get(i).toString());

    }
}
