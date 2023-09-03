package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Bank;

import java.sql.*;

/**
 * @author Sergey Kovalev
 * Класс выполняющий роль dao
 * и имплементирующий интерфейс:
 * @see BankDAO
 */
public class BankDAOImpl implements BankDAO {
    /**
     * Константа для SQL запроса поиска банка по id.
     */
    private final static String FIND_BY_ID = "SELECT * FROM banks WHERE id = ?";
    /**
     * Константа для SQL запроса поиска банка по имени.
     */
    private final static String FIND_BY_NAME = "SELECT * FROM banks WHERE name = ?";
    /**
     * Константа для SQL запроса сохранения нового банка.
     */
    private final static String SAVE_NEW_BANK = """
            INSERT INTO banks (name)
            VALUES
                (?);
            """;
    /**
     * Константа для SQL запроса обновления данных банка.
     */
    private final static String UPDATE_BANK = """
            UPDATE banks
            SET name = ?
            WHERE id = ?;
            """;
    /**
     * Константа для SQL запроса удаления банка по id.
     */
    private final static String DELETE_BANK_BY_ID = "DELETE FROM banks WHERE id = ?";

    /**
     * Имплементация метода получения банка по id.
     * @see Bank
     * @param id - id банка.
     * @return возвращает объект банка.
     */
    @Override
    public Bank getBankById(long id) {
        Bank bank = new Bank();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                fillBankData(resultSet, bank);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bank;
    }
    /**
     * Имплементация метода получения банка по имени.
     * @see Bank
     * @param name - имя банка.
     * @return возвращает объект банка.
     */
    @Override
    public Bank getBankByName(String name) {
        Bank bank = new Bank();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                fillBankData(resultSet, bank);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bank;
    }
    /**
     * Имплементация сохранения банка.
     * @see Bank
     * @param bank - принимает объект банка.
     * @return объект сохраненного банка.
     */
    @Override
    public Bank saveBank(Bank bank) {
        long id = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_NEW_BANK, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, bank.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            bank.setId(id);
            return bank;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Имплементация метода обновления данных банка.
     * @see Bank
     * @param bank - объект банка для изменения.
     * @return возвращает изменённый объкт банка.
     */
    @Override
    public Bank updateBank(Bank bank) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_BANK);
            statement.setString(1, bank.getName());
            statement.setLong(2, bank.getId());
            statement.executeUpdate();
            return getBankById(bank.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Имплементация удаления банка по id.
     * @param id - id банка для удаления из БД.
     * @return возвращает сообщение об успешном удалении.
     */
    @Override
    public String deleteBank(long id) {
        try(Connection connection = getConnection()) {
            Bank bank = getBankById(id);
            if (bank.getId() == 0) {
                return "Банка с таким id нет в базе данных";
            } else {
                PreparedStatement statement = connection.prepareStatement(DELETE_BANK_BY_ID);
                statement.setLong(1, id);
                statement.executeUpdate();
                return "Банк с id " + id + " удален.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Вспомогательный метод для заполнения данных объекта банка.
     * @param resultSet - список результатов полученных по SQL запросу.
     * @param bank - Объект банка для заполнения.
     * @throws SQLException - ошибка SQL запроса.
     */
    private void fillBankData(ResultSet resultSet, Bank bank) throws SQLException {
        bank.setId(resultSet.getLong("id"));
        bank.setName(resultSet.getString("name"));
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
