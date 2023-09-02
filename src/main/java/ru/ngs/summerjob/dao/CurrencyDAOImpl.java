package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Currency;

import java.sql.*;

public class CurrencyDAOImpl implements CurrencyDAO {
    private final static String FIND_BY_ID = "SELECT * FROM currencies WHERE id = ?";
    private final static String SAVE_NEW_CURRENCY = """
            INSERT INTO currencies (name)
            VALUES
                (?);
            """;
    private final static String UPDATE_CURRENCY = """
            UPDATE currencies
            SET name = ?
            WHERE id = ?;
            """;
    private final static String DELETE_CURRENCY_BY_ID = "DELETE FROM currencies WHERE id = ?";
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

    private void fillCurrencyData(ResultSet resultSet, Currency currency) throws SQLException {
        while (resultSet.next()) {
            currency.setId(resultSet.getLong("id"));
            currency.setName(resultSet.getString("name"));
        }
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
