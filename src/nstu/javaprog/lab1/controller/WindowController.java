package nstu.javaprog.lab1.controller;

import nstu.javaprog.lab1.environment.Habitat;

import java.awt.*;

public class WindowController {
    private Habitat habitat;

    public WindowController(Habitat habitat) {
        this.habitat = habitat;
    }

    public void activate() {
        habitat.removeAll();
        habitat.activate();
    }

    public void deactivate() {
        habitat.deactivate();
    }

    public long getCurrentTime() {
        return habitat.getTime();
    }

    public String getStatistic() {
        return habitat.getStatistic();
    }

    public void updateCanvas(Graphics graphics, int width, int height) {
        habitat.getAll().forEach((element) -> {
            element.normalize(width, height);
            element.draw(graphics);
            element.move();
        });
    }
}
