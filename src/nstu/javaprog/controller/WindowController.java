package nstu.javaprog.controller;

import javafx.util.Pair;
import nstu.javaprog.model.Fish;
import nstu.javaprog.model.Habitat;
import nstu.javaprog.util.Properties;

import java.util.List;
import java.util.function.Consumer;

public final class WindowController {
    private Habitat habitat = null;

    public final void prepare(Habitat habitat) {
        this.habitat = habitat;
    }

    public void activateGeneration() {
        habitat.activateGeneration();
    }

    public void deactivateGeneration() {
        habitat.deactivateGeneration();
    }

    public void activateGolds() {
        habitat.activateGolds();
    }

    public void deactivateGolds() {
        habitat.deactivateGolds();
    }

    public void activateGuppies() {
        habitat.activateGuppies();
    }

    public void deactivateGuppies() {
        habitat.deactivateGuppies();
    }

    public boolean isGenerationActivated() {
        return habitat.isGenerationActivated();
    }

    public boolean isGoldsActivated() {
        return habitat.isGoldsActivated();
    }

    public boolean isGuppiesActivated() {
        return habitat.isGuppiesActivated();
    }

    public void reset() {
        habitat.reset();
    }

    public Properties getGoldProperties() {
        return habitat.getGoldProperties();
    }

    public void setGoldProperties(Properties properties) {
        habitat.setGoldProperties(properties);
    }

    public Properties getGuppyProperties() {
        return habitat.getGuppyProperties();
    }

    public void setGuppyProperties(Properties properties) {
        habitat.setGuppyProperties(properties);
    }

    public List<Pair<Long, Integer>> getAliveElements() {
        return habitat.getAliveElements();
    }

    public int getCurrentTime() {
        return habitat.getTime();
    }

    public String getStatistic() {
        return habitat.getStatistic();
    }

    public void doForEachElement(Consumer<? super Fish> consumer) {
        habitat.doForEachElement(consumer);
    }
}
