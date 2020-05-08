import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Habitat extends JPanel {
    private final int maleSpawnHeight;
    private final int maleSpawnWidth;
    private final int workerSpawnHeight;
    private final int workerSpawnWidth;
    private final  int maleDefaultPeriod = 2;
    private final  int workerDefaultPeriod = 4;
    private final BeeFactory factory;
    private int countOfTicks = 0;
    private int maleChance = 70;
    private int workerChance = 50;
    private int maleTimePeriod;
    private int workerTimePeriod;
    private float K;
    private int beeMaleAmount = 0;
    private int beeWorkerAmount = 0;
    private boolean isDrawable = false;

    private Random rand = new Random();


    public Habitat(int spawnPlaceHeight, int spawnPlaceWidth) {
        maleSpawnHeight = spawnPlaceHeight - 100;
        maleSpawnWidth = spawnPlaceWidth - 150;
        workerSpawnHeight = spawnPlaceHeight - 100;
        workerSpawnWidth = spawnPlaceWidth - 150;
        maleTimePeriod = maleDefaultPeriod;
        workerTimePeriod = workerDefaultPeriod;
        setLayout(new BorderLayout());
        setBackground(new Color(166, 186, 233));
        setVisible(true);
        factory = new BeeFactory();
    }

    public void update() {
        countOfTicks++;
        if (maleChance >= rand.nextInt(100) && (countOfTicks % maleTimePeriod == 0)) {
            addBeeMale();
            isDrawable = true;
        }
        if (workerChance >= rand.nextInt(100) && (countOfTicks % workerTimePeriod == 0)) {
            addBeeWorker();
            isDrawable = true;
        }
        if (isDrawable) {
            repaint();
            isDrawable = false;
        }
    }

    public void killTransportList() {
        Singleton.INSTANCE.removeAll();
    }


    private void addBeeMale() {
        if(calcPercent(beeMaleAmount, beeMaleAmount + beeWorkerAmount) < K) {
            Singleton.INSTANCE.add(factory.createBeeMale(rand.nextInt(maleSpawnWidth - 50),
                    rand.nextInt(maleSpawnHeight - 75)));
            beeMaleAmount++;
        }
    }

    private void addBeeWorker() {
        Singleton.INSTANCE.add(factory.createBeeWorker(rand.nextInt(workerSpawnWidth - 50),
                rand.nextInt((workerSpawnHeight - 5) - 75)));
        beeWorkerAmount++;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Bee b : Singleton.INSTANCE.get()) {
            b.paint(g2);
        }

    }

    public int getCountOfTicks() {
        return countOfTicks;
    }

    public int getBeeMaleAmount() {
        return beeMaleAmount;
    }

    public int getBeeWorkerAmount() {
        return beeWorkerAmount;
    }

    public void setMaleChance(int maleChance) {
        this.maleChance = maleChance;
    }

    public void setWorkerChance(int workerChance) {
        this.workerChance = workerChance;
    }
    public void setKPercent(int newK) {
        this.K = newK;
    }

    public void setMaleTimePeriod(int timePeriodOfLC) {
        this.maleTimePeriod = timePeriodOfLC;
    }

    public void setWorkerTimePeriod(int timePeriodOfHC) {
        this.workerTimePeriod = timePeriodOfHC;
    }

    public int getMaleDefaultPeriod() {
        return maleDefaultPeriod;
    }

    public int getWorkerDefaultPeriod() {
        return workerDefaultPeriod;
    }

    private float calcPercent(int what, int howMuch) {  return ((float) what / (float) howMuch) * 100.0f; }
}