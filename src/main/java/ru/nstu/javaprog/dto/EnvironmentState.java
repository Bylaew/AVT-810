package ru.nstu.javaprog.dto;

public final class EnvironmentState {
    private final long currentId;
    private final int goldEntitiesNumber;
    private final int guppyEntitiesNumber;
    private final int generationTime;

    public EnvironmentState(
            long currentId,
            int goldEntitiesNumber,
            int guppyEntitiesNumber,
            int generationTime
    ) {
        this.currentId = currentId;
        this.goldEntitiesNumber = goldEntitiesNumber;
        this.guppyEntitiesNumber = guppyEntitiesNumber;
        this.generationTime = generationTime;
    }

    public int getGenerationTime() {
        return generationTime;
    }

    public int getGoldEntitiesNumber() {
        return goldEntitiesNumber;
    }

    public int getGuppyEntitiesNumber() {
        return guppyEntitiesNumber;
    }

    public long getCurrentId() {
        return currentId;
    }
}
