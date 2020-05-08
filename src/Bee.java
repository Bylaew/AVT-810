import javax.swing.*;
import java.awt.*;

public abstract class Bee extends JComponent implements IBehaviour {
    private float x, y;

    Bee() {
        x = 0;
        y = 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        try {
            g2.drawImage(this.getImage(), (int) this.x, (int) this.y, 100,150 ,null);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    Bee(float x, float y) {
        this.x = x;
        this.y = y;
    }

}