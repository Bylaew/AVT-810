import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat {
    private int JFwidth, JFheight;
    private ArrayList<Bee> objs;
    private BeeFactory factory;
    private Main myFrame;
    private int totalBees;
    private float N1, N2, K, P;
    private long timeFromStart = 0;
    private float N1time, N2time;
    private int workerAmount = 0, maleAmount = 0;
    private Timer startTime;
    private boolean firstRun = true;
    private BeeMale m;
    private long firstTime;
    private Painter painter;
    private JLabel timerLabel;
    Habitat(int JFwidth, int JFheight, float N1, float N2, float K, float P) {
        this.JFwidth = JFwidth;
        this.JFheight = JFheight;
        this.N1 = N1;
        this.N2 = N2;
        this.K = K;
        this.P = P;
        myFrame = new Main(JFwidth, JFheight);
        objs = new ArrayList<Bee>();
        factory = new BeeFactory();
        painter = new Painter();
        painter.setLayout(null);
        painter.setSize(JFwidth - 100, JFheight - 100);
        painter.setLocation(200, 200);
        painter.setBackground(Color.white);
        timerLabel = new JLabel();
        timerLabel.setBounds(10, JFheight, 700, 15);
        timerLabel.setText("Время: " + " Drones: " + maleAmount + " Workers: " + workerAmount);
        timerLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        myFrame.add(timerLabel);
    }

    public void init() {
        myFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    objs.clear();
                    painter.removeAll();
                    startTime = new Timer();
                    showPanel();
                    startTime.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (firstRun) {
                                firstTime = System.currentTimeMillis();
                                firstRun = false;
                            }
                            long currentTime = System.currentTimeMillis();
                            timeFromStart = (currentTime - firstTime);
                            timerLabel.setText("Time: " + timeFromStart / 1000.0f + "sec Drones: " + maleAmount + " Workers: " + workerAmount + " Total: " + totalBees + " Percent of males: "
                                    + calcPercent(maleAmount, totalBees) + " Percent of workers: " + calcPercent(workerAmount, totalBees));
                            update(timeFromStart);
                        }
                    }, 0, 100);
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    startTime.cancel();
                    startTime = null;
                    objs.clear();
                    totalBees = 0;
                    maleAmount = 0;
                    workerAmount = 0;
                    N1time = 0;
                    N2time = 0;
                    firstRun = true;
                    painter.removeAll();
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    if (timerLabel.isVisible()) {
                        timerLabel.setVisible(false);
                    } else {
                        timerLabel.setVisible(true);
                    }
                }
            }

        });
    }

    private void update(long timeFromStart) {
        if (totalBees != 0) {
            if ((timeFromStart > (N1time + N1 * 1000)) && (calcPercent(maleAmount, totalBees) < K)) {
                N1time += N1 * 1000;
                objs.add(factory.createBeeMale((float) Math.random() * painter.getWidth() - 50, (float) Math.random() * painter.getHeight() + 50));
                maleAmount++;
            }
        } else {
            objs.add(factory.createBeeMale((float) Math.random() * painter.getWidth() - 50, (float) Math.random() * painter.getHeight() + 50));
            maleAmount++;
        }
        if (timeFromStart > N2time + N2 * 1000) {
            N2time += N2 * 1000;
            if ((float) Math.random() <= P / 100.0f) {
                objs.add(factory.createBeeWorker((float) Math.random() * painter.getWidth() - 50, (float) Math.random() * painter.getHeight() + 50));
                workerAmount++;
            }
        }
        totalBees = maleAmount + workerAmount;
        painter.settingUp(objs);
        painter.paint(myFrame.getGraphics());
        painter.revalidate();
    }

    private float calcPercent(int what, int howMuch) {
        return ((float) what / (float) howMuch) * 100.0f;
    }

    private void showPanel() {
        painter.paintComponent(myFrame.getGraphics());
    }

}
