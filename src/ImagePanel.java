import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


 public class ImagePanel extends JPanel {


     private BufferedImage image;

     public void setImage(BufferedImage image) {
         this.image = image;
     }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

}
