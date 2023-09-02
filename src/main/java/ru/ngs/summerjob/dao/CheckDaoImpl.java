package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Check;

import java.sql.*;

public class CheckDaoImpl implements CheckDao {
    private final static String SAVE_NEW_NUMBER = """
            INSERT INTO checks
            VALUES (DEFAULT)
            RETURNING id;
            """;

    @Override
    public long saveCheck(Check check) {
        long checkNumber = 0;
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SAVE_NEW_NUMBER);
            while (resultSet.next()) {
                checkNumber = resultSet.getLong("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return checkNumber;
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(
                Config.getConfig().get("db").get("url"),
                Config.getConfig().get("db").get("login"),
                Config.getConfig().get("db").get("password")
        );
    }
}
