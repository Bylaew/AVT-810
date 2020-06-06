package ru.nstu.javaprog.repository;

import ru.nstu.javaprog.dto.EnvironmentState;

import java.sql.ResultSet;
import java.sql.SQLException;

final class EnvironmentStateExtractor {
    EnvironmentState extract(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return new EnvironmentState(
                resultSet.getLong("CURRENT_ID"),
                resultSet.getInt("GOLD_NUMBER"),
                resultSet.getInt("GUPPY_NUMBER"),
                resultSet.getInt("GENERATION_TIME")
        );
    }
}
