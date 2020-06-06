package ru.nstu.javaprog.api;

import ru.nstu.javaprog.model.Creator;
import ru.nstu.javaprog.model.gold.GoldCreator;
import ru.nstu.javaprog.model.guppy.GuppyCreator;

public enum FishType {
    GOLD("GOLD", new GoldCreator()),
    GUPPY("GUPPY", new GuppyCreator()),
    GENERAL("GENERAL", null);

    private final String type;
    private final Creator creator;

    FishType(String type, Creator creator) {
        this.type = type;
        this.creator = creator;
    }

    public String getType() {
        return type;
    }

    public Creator getCreator() {
        return creator;
    }

    public boolean equals(FishType fishType) {
        return fishType == GENERAL || this == fishType;
    }

    public static FishType getTypeByName(String name) {
        for (FishType fishType : FishType.values()) {
            if (fishType.type.equals(name)) {
                return fishType;
            }
        }
        throw new IllegalArgumentException("Unknown fish type");
    }
}
