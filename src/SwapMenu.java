import javax.swing.*;
import java.awt.*;

public class SwapMenu extends JPanel {
    JComboBox<Connection> connections = new JComboBox<>();
    JButton swap = new JButton("Обмен");

    SwapMenu(){
        setLayout(new GridLayout(4, 1));
        add(connections);
        add(swap);
        JFrame testframe = new JFrame();
        testframe.add(this);
        testframe.setVisible(true);
    }

    public static void main(String[] args) {
        SwapMenu test = new SwapMenu();
    }
}
