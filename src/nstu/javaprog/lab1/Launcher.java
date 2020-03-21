package nstu.javaprog.lab1;

import nstu.javaprog.lab1.controller.WindowController;
import nstu.javaprog.lab1.environment.Habitat;
import nstu.javaprog.lab1.view.canvas.MainWindow;

import java.awt.*;

public class Launcher extends Canvas {
    public static void main(String[] args) {
        new MainWindow(
                MainWindow.WINDOW_WIDTH,
                MainWindow.WINDOW_HEIGHT,
                "Aquarium",
                new WindowController(new Habitat())
        );
    }
}
