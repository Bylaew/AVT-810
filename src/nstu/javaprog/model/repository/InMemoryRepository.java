package nstu.javaprog.model.repository;

import javafx.util.Pair;
import nstu.javaprog.model.Fish;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.Consumer;

public final class InMemoryRepository {
    public static final InMemoryRepository INSTANCE = new InMemoryRepository();
    private final List<Fish> entities = Collections.synchronizedList(new LinkedList<>());
    private final Map<Long, Integer> generationTimes = Collections.synchronizedMap(new TreeMap<>());

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Set<Long> ids = new HashSet<>();

    private InMemoryRepository() {
    }

    public void add(Fish entity, int time) {
        entities.add(entity);
        generationTimes.put(entity.getId(), time);
        ids.add(entity.getId());
    }

    public void removeDeadEntities(int time) {
        final List<Fish> deadEntities = new LinkedList<>();
        entities.forEach((fish -> {
            if (generationTimes.get(fish.getId()) + fish.getLifetime() <= time)
                deadEntities.add(fish);
        }));
        for (Fish entity : deadEntities) {
            generationTimes.remove(entity.getId());
            ids.remove(entity.getId());
            entities.remove(entity);
        }
    }

    public List<Pair<Long, Integer>> getAliveEntities() {
        List<Pair<Long, Integer>> aliveEntities = new LinkedList<>();
        generationTimes.forEach((key, value) -> aliveEntities.add(new Pair<>(key, value)));
        return aliveEntities;
    }

    public void removeAll() {
        entities.clear();
        generationTimes.clear();
        ids.clear();
    }

    public void serialize(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(entities);
        outputStream.writeObject(generationTimes);
        outputStream.writeObject(ids);
    }

    @SuppressWarnings("unchecked")
    public void deserialize(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        try {
            InMemoryRepository.INSTANCE.entities.addAll((Collection<Fish>) inputStream.readObject());
            InMemoryRepository.INSTANCE.generationTimes.putAll((Map<Long, Integer>) inputStream.readObject());
            InMemoryRepository.INSTANCE.ids.addAll((Set<Long>) inputStream.readObject());
        } catch (ClassCastException exception) {
            throw new IOException(exception.getMessage());
        }
    }

    public void doForEachEntity(Consumer<Fish> consumer) {
        entities.forEach(consumer);
    }

    public int getEntitiesNumber() {
        return entities.size();
    }
}
