package nstu.javaprog;

import nstu.javaprog.controller.WindowController;
import nstu.javaprog.model.Habitat;
import nstu.javaprog.view.ViewContainer;

import javax.swing.*;

public final class Launcher {
    public static void main(String[] args) {
        Habitat habitat = new Habitat();

        WindowController windowController = new WindowController();
        windowController.prepare(habitat);

        SwingUtilities.invokeLater(() -> {
            ViewContainer viewContainer = new ViewContainer("Aquarium");
            viewContainer.prepare(windowController);
            viewContainer.setVisible(true);

            new Thread(() -> habitat.prepare(viewContainer)).start();
        });
    }
}
