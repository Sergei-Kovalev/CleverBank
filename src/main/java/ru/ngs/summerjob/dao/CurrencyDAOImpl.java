package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Currency;

import java.sql.*;

public class CurrencyDAOImpl implements CurrencyDAO {
    private final static String FIND_BY_ID = "SELECT * FROM currencies WHERE id = ?";
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

    private void fillCurrencyData(ResultSet resultSet, Currency currency) throws SQLException {
        while (resultSet.next()) {
            currency.setId(resultSet.getLong("id"));
            currency.setName(resultSet.getString("name"));
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
