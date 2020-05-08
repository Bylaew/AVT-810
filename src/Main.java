import javax.swing.*;

public class Main {
    final static int W_HEIGHT = 1000;
    final static int W_WIDTH = 1400;
    final static String W_NAME = "Bee's World";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI window = new MainGUI(W_NAME, W_HEIGHT, W_WIDTH);
        });
    }
}