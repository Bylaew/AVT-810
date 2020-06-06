package ru.nstu.javaprog.controller;

import javafx.util.Pair;
import ru.nstu.javaprog.api.FishType;
import ru.nstu.javaprog.model.Fish;
import ru.nstu.javaprog.model.Habitat;
import ru.nstu.javaprog.util.LinkedList;
import ru.nstu.javaprog.util.Properties;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class WindowController {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Habitat habitat;

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

    public void addFishToLinkedList(FishType fishType, long id) {
        habitat.createAndAddFishToLinkedList(fishType, id);
    }

    public void reset() {
        habitat.reset();
    }

    public void loadFromFile(File file, FishType fishType, Consumer<Exception> callback) {
        executorService.submit(() -> {
            Exception exception = null;
            try {
                habitat.reestablishFromFile(file, fishType);
            } catch (Exception e) {
                exception = e;
            }
            callback.accept(exception);
        });
    }

    public void loadToFile(File file, FishType fishType, Consumer<Exception> callback) {
        executorService.submit(() -> {
            Exception exception = null;
            try {
                habitat.loadToFile(file, fishType);
            } catch (Exception e) {
                exception = e;
            }
            callback.accept(exception);
        });
    }

    public void loadFromDatabase(String snapshotName, FishType fishType, Consumer<Exception> callback) {
        executorService.submit(() -> {
            Exception exception = null;
            try {
                habitat.reestablishFromDatabase(snapshotName, fishType);
            } catch (Exception e) {
                exception = e;
            }
            callback.accept(exception);
        });
    }

    public void loadToDatabase(String snapshotName, FishType fishType, Consumer<Exception> callback) {
        executorService.submit(() -> {
            Exception exception = null;
            try {
                habitat.loadToDatabase(snapshotName, fishType);
            } catch (Exception e) {
                exception = e;
            }
            callback.accept(exception);
        });
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

    public List<Pair<Long, Integer>> getAliveEntities() {
        return habitat.getAliveEntities();
    }

    public int getCurrentTime() {
        return habitat.getTime();
    }

    public String getStatistic() {
        return habitat.getStatistic();
    }

    public void saveGenerationSettings() {
        habitat.saveGenerationSettings();
    }

    public void doForEachEntity(Consumer<Fish> consumer) {
        habitat.doForEachEntity(consumer);
    }

    public LinkedList<Fish> getLinkedList() {
        return habitat.getLinkedList();
    }
}
