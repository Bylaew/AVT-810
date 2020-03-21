import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.imageio.*;

public class Habitat
{
    private Image image;
    private int width = 1280;
    private int height = 720;
    private Timer timer;
    private long time = 0;
    boolean sim_is_working = false;

    AbstractFactory factory = new RabbitFactory();
    int N1;
    double P1;
    int N2, K;
    Rabbit[] array;
    int count;

    public Habitat()
    {
        N1 = 2000;
        P1 = 0.75;
        N2 = 3000;
        K = 20;
        count = 0;
        array = new Rabbit[1000];

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
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setVisible(true);

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
                        if (!sim_is_working)
                        {
                            sim_is_working = true;
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Update(time,frame.getGraphics());
                                    time += 1000;
                                }
                            },1000, 1000);
                        }
                        break;
                    }
                    case KeyEvent.VK_E:
                    {
                        timer.cancel();
                        sim_is_working = false;
                        break;
                    }
                    case KeyEvent.VK_T:
                    {
                        JTextArea textArea = new JTextArea();
                        textArea.setFont(new Font("TimesRoman", Font.ITALIC, 25));
                        textArea.setText("Информация о симуляции:" + "\nВсего кроликов: " + count +
                                "\nОбычных: " + Ordinary_Rabbit.count +
                                "\nАльбиносов: " + Albino.count +
                                "\nВремя: " + (time/1000) + " секунд(ы)");
                        frame.add(textArea);
                        frame.setVisible(true);
                        array = new Rabbit[1000];
                        count = 0;
                        Ordinary_Rabbit.count = 0;
                        Albino.count = 0;
                        time = 0;
                        break;
                    }
                }
            }
        });
    }

    public void Update(long time, Graphics g)
    {
        g.drawImage(image,0,0,null);
        if (time % N1 == 0)
        {
            if (Math.random() < P1)
            {
                array[count] = factory.createOrdinary((float)(Math.random() * width),(float)(Math.random() * height));
                count++;
            }
        }
        if (time % N2 == 0)
        {
            double p = (double) (K * count) / 100;
            if ((double)Albino.count < p)
            {
                array[count] = factory.createAlbino((float)(Math.random() * width), (float)(Math.random() * height));
                count++;
            }
        }
        for (int i = 0; i < count; i++)
            g.drawImage(array[i].getImage(), (int) array[i].getX(), (int) array[i].getY(), null);
    }
}
