package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Currency;

import java.sql.*;

/**
 * @author Sergey Kovalev
 * Класс выполняющий роль dao
 * и имплементирующий интерфейс:
 * @see CurrencyDAO
 */
public class CurrencyDAOImpl implements CurrencyDAO {
    /**
     * Константа для SQL запроса поиска валюты по id.
     */
    private final static String FIND_BY_ID = "SELECT * FROM currencies WHERE id = ?";
    /**
     * Константа для SQL запроса сохранения новой валюты.
     */
    private final static String SAVE_NEW_CURRENCY = """
            INSERT INTO currencies (name)
            VALUES
                (?);
            """;
    /**
     * Константа для SQL запроса обновления данных валюты.
     */
    private final static String UPDATE_CURRENCY = """
            UPDATE currencies
            SET name = ?
            WHERE id = ?;
            """;
    /**
     * Константа для SQL запроса удаления валюты по id.
     */
    private final static String DELETE_CURRENCY_BY_ID = "DELETE FROM currencies WHERE id = ?";

    /**
     * Имплементация метода получения валюты по id.
     * @see Currency
     * @param id - id валюты.
     * @return возвращает объект валюты.
     */
    @Override
    public Currency getCurrencyById(long id) {
        Currency currency = new Currency();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            fillCurrencyData(resultSet, currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }
    /**
     * Имплементация сохранения валюты.
     * @see Currency
     * @param currency - принимает объект валюты.
     * @return объект сохраненной валюты.
     */
    @Override
    public Currency saveCurrency(Currency currency) {
        long id = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_NEW_CURRENCY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, currency.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            currency.setId(id);
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Имплементация метода обновления данных валюты.
     * @see Currency
     * @param currency - объект валюты для изменения.
     * @return возвращает изменённый объкт валюты.
     */
    @Override
    public Currency updateCurrency(Currency currency) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENCY);
            statement.setString(1, currency.getName());
            statement.setLong(2, currency.getId());
            statement.executeUpdate();
            return getCurrencyById(currency.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Имплементация удаления валюты по id.
     * @param id - id валюты для удаления из БД.
     * @return возвращает сообщение об успешном удалении.
     */
    @Override
    public String deleteCurrency(long id) {
        try(Connection connection = getConnection()) {
            Currency currency = getCurrencyById(id);
            if (currency.getId() == 0) {
                return "Наименования валюты с таким id нет в базе данных";
            } else {
                PreparedStatement statement = connection.prepareStatement(DELETE_CURRENCY_BY_ID);
                statement.setLong(1, id);
                statement.executeUpdate();
                return "Наименование валюты с id " + id + " удалено.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Вспомогательный метод для заполнения данных объекта валюты.
     * @param resultSet - список результатов полученных по SQL запросу.
     * @param currency - Объект валюты для заполнения.
     * @throws SQLException - ошибка SQL запроса.
     */
    private void fillCurrencyData(ResultSet resultSet, Currency currency) throws SQLException {
        while (resultSet.next()) {
            currency.setId(resultSet.getLong("id"));
            currency.setName(resultSet.getString("name"));
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
