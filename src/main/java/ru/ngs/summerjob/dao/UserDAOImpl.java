package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    private final static String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private final static String FIND_BY_LOGIN_AND_PASSWORD = "SELECT * FROM users WHERE login = ? AND password = ?";

    @Override
    public User getUserById(long id) {
        User user = new User();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            fillUserData(resultSet, user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        User user = new User();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN_AND_PASSWORD);
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            fillUserData(resultSet, user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private static void fillUserData(ResultSet resultSet, User user) throws SQLException {
        while (resultSet.next()) {
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setAccounts(null); //TODO добавить выборку аккаунтов
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Config.getConfig().get("db").get("url"),
                Config.getConfig().get("db").get("login"),
                Config.getConfig().get("db").get("password")
        );
    }
}
