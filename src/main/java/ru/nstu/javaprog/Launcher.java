package ru.nstu.javaprog;

import ru.nstu.javaprog.controller.WindowController;
import ru.nstu.javaprog.model.Habitat;
import ru.nstu.javaprog.view.ViewContainer;

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
        // 8 лабораторная
        new LinkedListTest().test();
    }
}
