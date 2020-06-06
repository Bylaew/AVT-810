package ru.nstu.javaprog.repository;

import javafx.util.Pair;
import ru.nstu.javaprog.api.FishType;
import ru.nstu.javaprog.dto.EntitiesSnapshot;
import ru.nstu.javaprog.model.Fish;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.Consumer;

public final class InMemoryRepository {
    public static final InMemoryRepository INSTANCE = new InMemoryRepository();
    private final List<Fish> entities = Collections.synchronizedList(new ArrayList<>());
    private final Map<Long, Integer> generationTimes = Collections.synchronizedMap(new HashMap<>());
    private final Set<Long> ids = Collections.synchronizedSet(new HashSet<>());

    private InMemoryRepository() {
    }

    public EntitiesSnapshot getSnapshot() {
        return new EntitiesSnapshot(entities, generationTimes, ids);
    }

    public void acceptSnapshot(EntitiesSnapshot entitiesSnapshot) {
        entities.clear();
        generationTimes.clear();
        ids.clear();
        entities.addAll(entitiesSnapshot.getEntities());
        generationTimes.putAll(entitiesSnapshot.getGenerationTimes());
        ids.addAll(entitiesSnapshot.getIds());
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

    public void serialize(ObjectOutputStream outputStream, FishType fishType) throws IOException {
        List<Fish> entitiesCopy = new ArrayList<>();
        Map<Long, Integer> generationTimesCopy = new HashMap<>();
        Set<Long> idsCopy = new HashSet<>();
        entities.forEach(entity -> {
            if (entity.getFishType().equals(fishType)) {
                entitiesCopy.add(entity);
                generationTimesCopy.put(entity.getId(), generationTimes.get(entity.getId()));
                idsCopy.add(entity.getId());
            }
        });
        outputStream.writeObject(entitiesCopy);
        outputStream.writeObject(generationTimesCopy);
        outputStream.writeObject(idsCopy);
    }

    @SuppressWarnings("unchecked")
    public void deserialize(ObjectInputStream inputStream, FishType fishType) throws IOException, ClassNotFoundException {
        try {
            List<Fish> entitiesCopy = (List<Fish>) inputStream.readObject();
            Map<Long, Integer> generationTimesCopy = (Map<Long, Integer>) inputStream.readObject();
            entitiesCopy.forEach(entity -> {
                if (entity.getFishType().equals(fishType)) {
                    InMemoryRepository.INSTANCE.entities.add(entity);
                    InMemoryRepository.INSTANCE.generationTimes.put(
                            entity.getId(), generationTimesCopy.get(entity.getId())
                    );
                    InMemoryRepository.INSTANCE.ids.add(entity.getId());
                }
            });
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
