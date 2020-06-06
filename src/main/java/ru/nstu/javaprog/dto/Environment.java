package ru.nstu.javaprog.dto;

public final class Environment {
    private final EnvironmentState environmentState;
    private final EntitiesSnapshot entitiesSnapshot;

    public Environment(EnvironmentState environmentState, EntitiesSnapshot entitiesSnapshot) {
        this.environmentState = environmentState;
        this.entitiesSnapshot = entitiesSnapshot;
    }

    public EntitiesSnapshot getEntitiesSnapshot() {
        return entitiesSnapshot;
    }

    public EnvironmentState getEnvironmentState() {
        return environmentState;
    }
}
