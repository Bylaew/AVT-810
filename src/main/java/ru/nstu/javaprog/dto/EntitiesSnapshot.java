package ru.nstu.javaprog.dto;

import ru.nstu.javaprog.model.Fish;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class EntitiesSnapshot {
    private final List<Fish> entities;
    private final Map<Long, Integer> generationTimes;
    private final Set<Long> ids;

    public EntitiesSnapshot(List<Fish> entities, Map<Long, Integer> generationTimes, Set<Long> ids) {
        this.entities = entities;
        this.generationTimes = generationTimes;
        this.ids = ids;
    }

    public List<Fish> getEntities() {
        return entities;
    }

    public Map<Long, Integer> getGenerationTimes() {
        return generationTimes;
    }

    public Set<Long> getIds() {
        return ids;
    }
}
