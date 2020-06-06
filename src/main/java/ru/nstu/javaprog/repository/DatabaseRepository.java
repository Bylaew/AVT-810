package ru.nstu.javaprog.repository;

import ru.nstu.javaprog.api.FishType;
import ru.nstu.javaprog.dto.Environment;
import ru.nstu.javaprog.model.Fish;

import java.sql.*;

public final class DatabaseRepository {
    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException exception) {
            System.out.println("H2 driver was not found");
        }
    }

    public static final DatabaseRepository INSTANCE = new DatabaseRepository();

    private final Connection connection;
    private final EntitiesSnapshotExtractor entitiesSnapshotExtractor = new EntitiesSnapshotExtractor();
    private final EnvironmentStateExtractor environmentStateExtractor = new EnvironmentStateExtractor();

    private DatabaseRepository() {
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/aquarium", "sa", "");
            prepare();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            throw new IllegalStateException("Database connection failed", exception);
        }
    }

    private void prepare() throws SQLException {
        String createFishesTableSql = "CREATE TABLE IF NOT EXISTS FISHES (" +
                "SNAPSHOT_NAME VARCHAR(128)," +
                "FISH_ID BIGINT," +
                "TYPE_NAME VARCHAR(128)," +
                "BIRTH_TIME INTEGER," +
                "LIFETIME INTEGER," +
                "X INTEGER," +
                "Y INTEGER," +
                "X_SPEED INTEGER," +
                "Y_SPEED INTEGER," +
                "PRIMARY KEY(SNAPSHOT_NAME, FISH_ID)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createFishesTableSql);
        }

        String createSettingsTableSql = "CREATE TABLE IF NOT EXISTS SETTINGS (" +
                "SNAPSHOT_NAME VARCHAR(128)," +
                "GOLD_NUMBER INTEGER," +
                "GUPPY_NUMBER INTEGER," +
                "GENERATION_TIME INTEGER," +
                "CURRENT_ID BIGINT," +
                "PRIMARY KEY(SNAPSHOT_NAME)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createSettingsTableSql);
        }
    }

    public void serialize(String snapshotName, Environment environment, FishType type) throws SQLException {
        String deleteSnapshotFromFishesRequest = "DELETE FROM FISHES WHERE SNAPSHOT_NAME = '" + snapshotName + "'";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteSnapshotFromFishesRequest);
        }

        String deleteSnapshotFromSettingsRequest = "DELETE FROM SETTINGS WHERE SNAPSHOT_NAME = '" + snapshotName + "'";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteSnapshotFromSettingsRequest);
        }

        String insertSettingsRequest = "INSERT INTO SETTINGS (" +
                "SNAPSHOT_NAME," +
                "GOLD_NUMBER," +
                "GUPPY_NUMBER," +
                "GENERATION_TIME," +
                "CURRENT_ID" +
                ")" +
                " VALUES (" +
                "'" + snapshotName + "'," +
                environment.getEnvironmentState().getGoldEntitiesNumber() + "," +
                environment.getEnvironmentState().getGuppyEntitiesNumber() + "," +
                environment.getEnvironmentState().getGenerationTime() + "," +
                environment.getEnvironmentState().getCurrentId() +
                ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertSettingsRequest);
        }

        String insertFishesRequest = "INSERT INTO FISHES (" +
                "SNAPSHOT_NAME," +
                "FISH_ID," +
                "TYPE_NAME," +
                "BIRTH_TIME," +
                "LIFETIME," +
                "X," +
                "Y," +
                "X_SPEED," +
                "Y_SPEED" +
                ")" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertFishesRequest)) {
            preparedStatement.setString(1, snapshotName);
            for (Fish fish : environment.getEntitiesSnapshot().getEntities()) {
                if (fish.getFishType().equals(type)) {
                    preparedStatement.setLong(2, fish.getId());
                    preparedStatement.setString(3, fish.getFishType().getType());
                    preparedStatement.setInt(
                            4,
                            environment.getEntitiesSnapshot().getGenerationTimes().get(fish.getId())
                    );
                    preparedStatement.setInt(5, fish.getLifetime());
                    preparedStatement.setInt(6, fish.getX());
                    preparedStatement.setInt(7, fish.getY());
                    preparedStatement.setInt(8, fish.getXSpeed());
                    preparedStatement.setInt(9, fish.getYSpeed());
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    public Environment deserialize(String snapshotName, FishType fishType) throws SQLException {
        String selectFishesRequest = "SELECT * FROM FISHES WHERE SNAPSHOT_NAME = '" + snapshotName + "'";
        String selectSettingsRequest = "SELECT * FROM SETTINGS WHERE SNAPSHOT_NAME = '" + snapshotName + "'";
        try (Statement environmentStateStatement = connection.createStatement();
             ResultSet environmentStateResultSet = environmentStateStatement.executeQuery(selectSettingsRequest);
             Statement entitiesSnapshotStatement = connection.createStatement();
             ResultSet entitiesSnapshotResultSet = entitiesSnapshotStatement.executeQuery(selectFishesRequest)
        ) {
            return new Environment(
                    environmentStateExtractor.extract(environmentStateResultSet),
                    entitiesSnapshotExtractor.extract(entitiesSnapshotResultSet, fishType)
            );
        }
    }
}
