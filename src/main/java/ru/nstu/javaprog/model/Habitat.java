package ru.nstu.javaprog.model;

import javafx.util.Pair;
import ru.nstu.javaprog.api.FishType;
import ru.nstu.javaprog.dto.Environment;
import ru.nstu.javaprog.dto.EnvironmentState;
import ru.nstu.javaprog.exception.IllegalPropertiesException;
import ru.nstu.javaprog.model.gold.GoldBehavior;
import ru.nstu.javaprog.model.gold.GoldProbabilisticCreator;
import ru.nstu.javaprog.model.guppy.GuppyBehavior;
import ru.nstu.javaprog.model.guppy.GuppyProbabilisticCreator;
import ru.nstu.javaprog.repository.DatabaseRepository;
import ru.nstu.javaprog.repository.InMemoryRepository;
import ru.nstu.javaprog.util.LinkedList;
import ru.nstu.javaprog.util.Properties;
import ru.nstu.javaprog.view.ViewContainer;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Habitat {
    private final TickGenerator generator = new TickGenerator();
    private final ru.nstu.javaprog.util.LinkedList<Fish> linkedList = new ru.nstu.javaprog.util.LinkedList<>();
    private ProbabilisticCreator goldCreator;
    private ProbabilisticCreator guppyCreator;
    private Behavior goldBehavior;
    private Behavior guppyBehavior;
    private ViewContainer view;

    public final void prepare(ViewContainer view) {
        try (Scanner scanner = new Scanner(new File("./resources/habitat.cfg"))) {
            scanner.useLocale(Locale.US);
            goldCreator = new GoldProbabilisticCreator(Properties.buildPropertiesWithCheckedException(
                    scanner.nextFloat(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt()
            ));

            guppyCreator = new GuppyProbabilisticCreator(Properties.buildPropertiesWithCheckedException(
                    scanner.nextFloat(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt()
            ));
        } catch (FileNotFoundException | IllegalPropertiesException
                | NoSuchElementException exception) {

            System.err.println("Couldn't load habitat.cfg");
            goldCreator = new GoldProbabilisticCreator(Properties.buildPropertiesWithUncheckedException(
                    1.0f,
                    1,
                    1,
                    1,
                    5,
                    Thread.NORM_PRIORITY
            ));

            guppyCreator = new GuppyProbabilisticCreator(Properties.buildPropertiesWithUncheckedException(
                    1.0f,
                    1,
                    1,
                    1,
                    5,
                    Thread.NORM_PRIORITY
            ));
        }
        this.view = view;

        guppyCreator.prepare(this);
        goldCreator.prepare(this);

        goldBehavior = new GoldBehavior(33);
        goldBehavior.prepare(this, view);

        guppyBehavior = new GuppyBehavior(33);
        guppyBehavior.prepare(this, view);

        generator.schedule();
    }

    public void doForEachEntity(Consumer<Fish> consumer) {
        InMemoryRepository.INSTANCE.doForEachEntity(consumer);
    }

    public int getTime() {
        return generator.getTime();
    }

    public String getStatistic() {
        return "Current: " + InMemoryRepository.INSTANCE.getEntitiesNumber() + '\n' +
                "Total golds: " + goldCreator.getEntitiesNumber() + '\n' +
                "Total guppies: " + guppyCreator.getEntitiesNumber() + '\n' +
                "Time: " + getTime();
    }

    public Properties getGoldProperties() {
        return goldCreator.getProperties();
    }

    public void setGoldProperties(Properties properties) {
        goldCreator.setProperties(properties);
        goldBehavior.changePriority(properties.getPriority());
    }

    public void createAndAddFishToLinkedList(FishType fishType, long id) {
        linkedList.add(fishType.getCreator().createFish(id, 0, 0, 0, 0, 1));
    }

    public LinkedList<Fish> getLinkedList() {
        return linkedList;
    }

    public Properties getGuppyProperties() {
        return guppyCreator.getProperties();
    }

    public void setGuppyProperties(Properties properties) {
        guppyCreator.setProperties(properties);
        guppyBehavior.changePriority(properties.getPriority());
    }

    public void activateGeneration() {
        generator.activate();
    }

    public void deactivateGeneration() {
        generator.deactivate();
    }

    public void activateGolds() {
        goldBehavior.activate();
    }

    public void deactivateGolds() {
        goldBehavior.deactivate();
    }

    public void activateGuppies() {
        guppyBehavior.activate();
    }

    public void deactivateGuppies() {
        guppyBehavior.deactivate();
    }

    public List<Pair<Long, Integer>> getAliveEntities() {
        return InMemoryRepository.INSTANCE.getAliveEntities();
    }

    public void reset() {
        generator.time.set(0);
        goldCreator.resetEntitiesNumber();
        guppyCreator.resetEntitiesNumber();
        ProbabilisticCreator.resetIdPool();
        InMemoryRepository.INSTANCE.removeAll();
    }

    public boolean isGenerationActivated() {
        return generator.isActivated();
    }

    public boolean isGoldsActivated() {
        return goldBehavior.isActivated();
    }

    public boolean isGuppiesActivated() {
        return guppyBehavior.isActivated();
    }

    public void reestablishFromFile(File file, FishType fishType) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {

            ProbabilisticCreator.setPrimaryId(inputStream.readLong());
            goldCreator.setEntitiesNumber(inputStream.readInt());
            guppyCreator.setEntitiesNumber(inputStream.readInt());
            generator.setTime(inputStream.readInt());
            InMemoryRepository.INSTANCE.deserialize(inputStream, fishType);
        }
    }

    public void loadToFile(File file, FishType fishType) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {

            outputStream.writeLong(ProbabilisticCreator.getCurrentId());
            outputStream.writeInt(goldCreator.getEntitiesNumber());
            outputStream.writeInt(guppyCreator.getEntitiesNumber());
            outputStream.writeInt(generator.getTime());
            InMemoryRepository.INSTANCE.serialize(outputStream, fishType);
        }
    }

    public void reestablishFromDatabase(String snapshotName, FishType fishType) throws SQLException {
        Environment environment = DatabaseRepository.INSTANCE.deserialize(snapshotName, fishType);
        ProbabilisticCreator.setPrimaryId(environment.getEnvironmentState().getCurrentId());
        goldCreator.setEntitiesNumber(environment.getEnvironmentState().getGoldEntitiesNumber());
        guppyCreator.setEntitiesNumber(environment.getEnvironmentState().getGuppyEntitiesNumber());
        generator.setTime(environment.getEnvironmentState().getGenerationTime());
        InMemoryRepository.INSTANCE.acceptSnapshot(environment.getEntitiesSnapshot());
    }

    public void loadToDatabase(String snapshotName, FishType fishType) throws SQLException {
        DatabaseRepository.INSTANCE.serialize(
                snapshotName,
                new Environment(
                        new EnvironmentState(
                                ProbabilisticCreator.getCurrentId(),
                                goldCreator.getEntitiesNumber(),
                                guppyCreator.getEntitiesNumber(),
                                generator.getTime()
                        ),
                        InMemoryRepository.INSTANCE.getSnapshot()
                ),
                fishType
        );
    }

    public void saveGenerationSettings() {
        try (PrintWriter writer = new PrintWriter(new File("./resources/habitat.cfg"))) {
            writer.println(goldCreator.getProperties().getChance());
            writer.println(goldCreator.getProperties().getDelay());
            writer.println(goldCreator.getProperties().getMinSpeed());
            writer.println(goldCreator.getProperties().getMaxSpeed());
            writer.println(goldCreator.getProperties().getLifetime());
            writer.println(goldCreator.getProperties().getPriority());

            writer.println(guppyCreator.getProperties().getChance());
            writer.println(guppyCreator.getProperties().getDelay());
            writer.println(guppyCreator.getProperties().getMinSpeed());
            writer.println(guppyCreator.getProperties().getMaxSpeed());
            writer.println(guppyCreator.getProperties().getLifetime());
            writer.println(guppyCreator.getProperties().getPriority());
        } catch (FileNotFoundException exception) {
            System.err.println("Couldn't unload settings to habitat.cfg");
        }
    }

    private class TickGenerator {
        final AtomicInteger time = new AtomicInteger();
        volatile boolean isSuspended = true;

        void schedule() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended) {
                        Fish entity;

                        InMemoryRepository.INSTANCE.removeDeadEntities(getTime());

                        if ((entity = goldCreator.createEntity()) != null)
                            InMemoryRepository.INSTANCE.add(entity, getTime());

                        if ((entity = guppyCreator.createEntity()) != null)
                            InMemoryRepository.INSTANCE.add(entity, getTime());

                        time.incrementAndGet();
                        view.updateTime();
                    }
                }
            }, 0, 1000);
        }

        int getTime() {
            return time.get();
        }

        void setTime(int value) {
            time.set(value);
        }

        void activate() {
            isSuspended = false;
        }

        void deactivate() {
            isSuspended = true;
        }

        boolean isActivated() {
            return !isSuspended;
        }
    }
}
