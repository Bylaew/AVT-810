package nstu.javaprog;

import nstu.javaprog.controller.WindowController;
import nstu.javaprog.model.Habitat;
import nstu.javaprog.view.ViewContainer;

import javax.swing.*;

public final class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new ViewContainer(
                        "Aquarium",
                        new WindowController(new Habitat())
                )
        );
    }
}
