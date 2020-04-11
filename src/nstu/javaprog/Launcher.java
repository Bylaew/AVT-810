package nstu.javaprog;

import nstu.javaprog.controller.WindowController;
import nstu.javaprog.model.Habitat;
import nstu.javaprog.view.ViewContainer;

public final class Launcher {
    public static void main(String[] args) {
        new ViewContainer(
                "Aquarium",
                new WindowController(new Habitat())
        );
    }
}
