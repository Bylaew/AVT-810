package nstu.javaprog;

import nstu.javaprog.controller.WindowController;
import nstu.javaprog.model.Habitat;
import nstu.javaprog.view.ViewContainer;

import javax.swing.*;

public final class Launcher {
    public static void main(String[] args) {
        final Habitat habitat = new Habitat();
        habitat.prepare();

        final WindowController windowController = new WindowController();
        windowController.prepare(habitat);

        SwingUtilities.invokeLater(() -> {
            ViewContainer viewContainer = new ViewContainer("Aquarium");
            ViewContainer.setDefaultLookAndFeelDecorated(true);
            viewContainer.prepare(windowController);
            viewContainer.setVisible(true);
        });
    }
}
