import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Habitat {
    private int width;
    private int height;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private Timer startTime;
    private long timeFromStart = 0;
    private float N1, N2, P1, P2;
    private CarFactory carFactory;
    private MotoFactory motoFactory;
    private boolean isRunning = false;


    Habitat() {
        this.height = 800;
        this.width = 1200;
        carFactory = new CarFactory();
        motoFactory = new MotoFactory();
    }

    public void init(float N1, float N2, float P1, float P2) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.P2 = P2;
        JFrame window = new JFrame("Lab 1");
        window.setSize(width, height);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        JLabel labelTime = new JLabel();
        labelTime.setText("0:00");
        labelTime.setVisible(false);
        labelTime.setFont(new Font("Consolas", Font.PLAIN, 60));
        panel.add(labelTime);

        window.add(panel);


        startTime = new Timer(1000, e -> {
            timeFromStart++;
            labelTime.setText(timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);
            update(timeFromStart, window);
        });

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    if (!isRunning) {
                        isRunning = true;
                        System.out.println("Start");
                        startTime.start();
                        vehicles = new ArrayList<>();
                    }
                }
                    else if (e.getKeyCode() == KeyEvent.VK_E) {
                    startTime.stop();
                    statistic(window);
                    window.repaint();
                    Moto.count = 0;
                    Car.count = 0;
                    timeFromStart = 0;
                    labelTime.setText(timeFromStart / 60 + ":" + "0" + timeFromStart % 60);
                    isRunning = false;
                    vehicles = null;
                }
                else if (e.getKeyCode() == KeyEvent.VK_T) {
                        if (labelTime.isVisible())
                            labelTime.setVisible(false);
                        else labelTime.setVisible(true);
                }

            }
        });
        window.setVisible(true);
    }


    private void statistic(JFrame window) {
        System.out.println("Statistics");
        JDialog dialog = new JDialog(window, "Statistics");

        var textField1 = new JTextField("Cars: " + Car.count);
        var textField2 = new JTextField("Motorcycles: " + Moto.count);
        var textField3 = new JTextField("Total objects: " + (Car.count + Moto.count));
        var textField4 = new JTextField("Work Time: " + timeFromStart / 60 + ":" + ((timeFromStart % 60 < 10) ? "0" : "") + timeFromStart % 60);

        dialog.setLayout(new GridLayout(4, 1));

        textField1.setEditable(false);
        textField2.setEditable(false);
        textField3.setEditable(false);
        textField4.setEditable(false);

        textField1.setFont(new Font("Consolas", Font.PLAIN, 22));
        textField2.setFont(new Font("Consolas", Font.PLAIN, 22));
        textField3.setFont(new Font("Consolas", Font.PLAIN, 22));
        textField4.setFont(new Font("Consolas", Font.PLAIN, 22));
        dialog.add(textField1);
        dialog.add(textField2);
        dialog.add(textField3);
        dialog.add(textField4);
        dialog.add(textField4);

        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);
    }



    private void update(float time, JFrame window) {
        if (time % N1 == 0) {
            if (Math.random() < P1) {
                vehicles.add(carFactory.createVehicle((float) (Math.random() * (width-200)), (float) Math.random() * (height-100)));
                System.out.println("Car created " + vehicles.get(vehicles.size()-1).getX() + "," + vehicles.get(vehicles.size()-1).getY() );
            }
        }
        if (time % N2 == 0) {
            if (Math.random() < P2) {
                vehicles.add(motoFactory.createVehicle((float) Math.random() * (width-200), (float) Math.random() * (height-100)));
                System.out.println("Motorcycle created "+ vehicles.get(vehicles.size()-1).getX() + "," + vehicles.get(vehicles.size()-1).getY() );
            }
            for(Vehicle a: vehicles)
                window.getGraphics().drawImage(a.getImage(), (int) a.getX(), (int)a.getY(), a.getImage().getWidth(null),  a.getImage().getHeight(null), null);
        }


    }
}
