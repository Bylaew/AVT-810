package nstu.javaprog.controller;

import nstu.javaprog.model.CanvasElement;
import nstu.javaprog.model.Habitat;
import nstu.javaprog.util.Properties;

import java.awt.*;

public final class WindowController {
    private final Habitat habitat;

    public WindowController(Habitat habitat) {
        this.habitat = habitat;
    }

    public void activate() {
        habitat.activate();
    }

    public void deactivate() {
        habitat.deactivate();
    }

    public boolean isLaunched() {
        return habitat.isLaunched();
    }

    public void reset() {
        habitat.reset();
    }

    public Properties getGoldProperties() {
        return habitat.getGoldProperties();
    }

    public Properties getGuppyProperties() {
        return habitat.getGuppyProperties();
    }

    public void setGoldProperties(Properties properties) {
        habitat.setGoldProperties(properties);
    }

    public void setGuppyProperties(Properties properties) {
        habitat.setGuppyProperties(properties);
    }

    public long getCurrentTime() {
        return habitat.getTime();
    }

    public String getStatistic() {
        return habitat.getStatistic();
    }

    public void moveElements() {
        habitat.doForEachElement(CanvasElement::move);
    }

    public void drawElements(Graphics graphics, int width, int height) {
        habitat.doForEachElement((element) -> {
            element.normalize(width, height);
            element.draw(graphics);
        });
    }
}
