import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat
{
    //private Image background;
    private int width;
    private int height;
    private StoneFactory stoneFactory;
    private WoodFactory woodFactory;
    boolean simulation=false;
    private Timer timer;
    private float time;
    House[] array;
    private int i;

    //условия: вероятности и время
    private float t1,t2;
    private double p1,p2;

    Habitat()
    {
        this.height=720;
        this.width=1280;
        stoneFactory= new StoneFactory();
        woodFactory= new WoodFactory();
        JFrame frame = new JFrame("Simulation");
        frame.setSize(width,height);
        frame.setVisible(true);
        frame.setResizable(false);
        i=0;t1=10;t2=3;p1=1;p2=0.7;
        time=0;
        timer=new Timer();
        array = new House[1500];

        JPanel panel= new JPanel();
        JTextArea text =new JTextArea();
        panel.add(text);
        frame.add(panel);
        //text.setPreferredSize(new Dimension(50,50));
        text.setVisible(false);
        text.setFont(new Font("Helvetica",Font.BOLD,14));

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent code) {
                super.keyPressed(code);
                int key=code.getKeyCode();
                switch(key)
                {
                    case KeyEvent.VK_B:
                    {
                        if (simulation==false)
                        {
                            simulation=true;
                            TimerTask tt = new TimerTask()
                            {
                                @Override
                                public void run() {
                                    update(time,frame);time=time+1000; text.setText("Время:" + time/1000 );
                                }
                            };
                            timer.schedule(tt,0,1000);
                        }
                    }break;
                    case KeyEvent.VK_E:
                    {
                        timer.cancel();
                        simulation=false;

                        text.setEditable(false);

                        text.setFont(new Font("TimesRoman", Font.ITALIC, 30));
                        text.setText("Всего домов: " + i +
                                "\nДеревянные: " + Wood.counter +
                                "\nКаменные: " + Stone.counter +
                                "\nВремя: " + (time/1000) + " секунд");
                        frame.repaint();
                        break;
                    }
                    case KeyEvent.VK_T:
                    {
                        if (!text.isVisible())
                        { text.setVisible(true); }
                        else {text.setVisible(false);break; }

                    }
                }
            }
        });

    }
    public void update (float time, JFrame frame)
    {
        if (time/1000 % t1 == 0 && time!=0 )
        {
            if (Math.random() < p1)
            {
                array[i]=stoneFactory.create((int)(Math.random()*width),(int)(Math.random()*height));
                frame.getGraphics().drawImage(array[i].getImage(), array[i].getX(), array[i].getY(), null);
                i++;
            }
        }
        if (time/1000 % t2 == 0 && time!=0 ) {
            if (Math.random() < p2) {
                array[i] = woodFactory.create((int) (Math.random() * width), (int) (Math.random() * height));
                frame.getGraphics().drawImage(array[i].getImage(), array[i].getX(), array[i].getY(), null);
                i++;
            }
        }
    }
}
