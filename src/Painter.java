import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Painter extends JPanel {
    private ArrayList<Bee> objs;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    void settingUp(ArrayList<Bee> o) {
        objs = o;
    }

    public void paint(Graphics g) {
        try {
            for (Bee bee : this.objs) {
                //if(bee instanceof BeeMale || bee instanceof BeeWorker)
                    g.drawImage(bee.getImage(), (int) bee.getX(), (int) bee.getY(), 100, 150, (ImageObserver) null);
            }

        } catch (Exception e) {

        }
    }


}
