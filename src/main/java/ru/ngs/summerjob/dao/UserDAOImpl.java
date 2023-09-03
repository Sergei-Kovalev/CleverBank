package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.User;

import java.sql.*;

/**
 * @author Sergey Kovalev
 * Класс выполняющий роль dao
 * и имплементирующий интерфейс:
 * @see UserDAO
 */
public class UserDAOImpl implements UserDAO {
    /**
     * Константа для SQL запроса поиска клиента по id.
     */
    private final static String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    /**
     * Константа для SQL запроса поиска клиента по логину и паролю.
     */
    private final static String FIND_BY_LOGIN_AND_PASSWORD = "SELECT * FROM users WHERE login = ? AND password = ?";
    /**
     * Константа для SQL запроса сохранения нового клиента.
     */
    private final static String SAVE_NEW_USER = """
            INSERT INTO users(name, login, password)
            VALUES
                (?, ?, ?);
            """;
    /**
     * Константа для SQL запроса обновления данных клиента.
     */
    private final static String UPDATE_USER = """
            UPDATE users
            SET name = ?, login = ?, password = ?
            WHERE id = ?;
            """;
    /**
     * Константа для SQL запроса удаления клиента по id.
     */
    private final static String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";

    /**
     * Имплементация метода получения клиента по id.
     * @see User
     * @param id - id клиента.
     * @return возвращает объект клиента.
     */
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

    /**
     * Имплементация сохранения клиента.
     * @see User
     * @param user - принимает объект клиента.
     * @return объект сохраненного клиента.
     */
    public User saveUser(User user) {
        long id = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_NEW_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            user.setId(id);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Имплементация метода обновления данных клиента.
     * @see User
     * @param user - объект клиента для изменения.
     * @return возвращает изменённый объкт клиента.
     */
    public User updateUser(User user) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
            return getUserById(user.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Имплементация удаления клиента по id.
     * @param id - id клиента для удаления из БД.
     * @return возвращает сообщение об успешном удалении.
     */
    public String deleteUserById(long id) {
        try(Connection connection = getConnection()) {
            User user = getUserById(id);
            if (user.getId() == 0) {
                return "Клиента с таким id нет в базе данных";
            } else {
                PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID);
                statement.setLong(1, id);
                statement.executeUpdate();
                return "Клиент с id " + id + " удален.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Имплементация метода получения клиента по логину и паролю.
     * @see User
     * @param login - логин клиента.
     * @param password - пароль клиента.
     * @return объект клиента.
     */
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

    /**
     * Вспомогательный метод для заполнения данных объекта клиента.
     * @param resultSet - список результатов полученных по SQL запросу.
     * @param user - Объект клиента для заполнения.
     * @throws SQLException - ошибка SQL запроса.
     */
    private static void fillUserData(ResultSet resultSet, User user) throws SQLException {
        while (resultSet.next()) {
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
        }
    }

    /**
     * Вспомогательный метод для получения подключения к базе данных.
     * @return возращает объект для подключения к БД.
     * @throws SQLException - ошибка SQL запроса.
     */
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
