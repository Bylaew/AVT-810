import javax.swing.*;

public class Main extends JFrame {
    Main(int width, int height){
        super("LR1VAR2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width,height);
        setLayout(null);
        setContentPane(new JPanel());
        setVisible(true);
    }

    public static void main(String[] args){
        Habitat habitat = new Habitat(1024,768,1,1,30,80);
        habitat.init();
    }
}