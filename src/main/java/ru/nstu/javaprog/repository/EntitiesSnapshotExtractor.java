package ru.nstu.javaprog.repository;

import ru.nstu.javaprog.api.FishType;
import ru.nstu.javaprog.dto.EntitiesSnapshot;
import ru.nstu.javaprog.model.Fish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

final class EntitiesSnapshotExtractor {
    EntitiesSnapshot extract(ResultSet resultSet, FishType requiredFishType) throws SQLException {
        List<Fish> fishes = new ArrayList<>();
        Map<Long, Integer> generationTimes = new HashMap<>();
        Set<Long> ids = new HashSet<>();
        while (resultSet.next()) {
            FishType fishType = FishType.getTypeByName(resultSet.getString("TYPE_NAME"));
            if (fishType.equals(requiredFishType)) {
                fishes.add(fishType.getCreator().createFish(
                        resultSet.getLong("FISH_ID"),
                        resultSet.getInt("X"),
                        resultSet.getInt("Y"),
                        resultSet.getInt("X_SPEED"),
                        resultSet.getInt("Y_SPEED"),
                        resultSet.getInt("LIFETIME")
                ));
                generationTimes.put(resultSet.getLong("FISH_ID"), resultSet.getInt("BIRTH_TIME"));
                ids.add(resultSet.getLong("FISH_ID"));
            }
        }
        return new EntitiesSnapshot(fishes, generationTimes, ids);
    }
}
