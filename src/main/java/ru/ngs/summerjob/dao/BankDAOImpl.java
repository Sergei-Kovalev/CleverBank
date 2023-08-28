package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Bank;

import java.sql.*;

public class BankDAOImpl implements BankDAO {
    private final static String FIND_BY_ID = "SELECT * FROM banks WHERE id = ?";
    @Override
    public Bank getBankById(long id) {
        Bank bank = new Bank();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            fillBankData(resultSet, bank);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bank;
    }

    private void fillBankData(ResultSet resultSet, Bank bank) throws SQLException {
        while (resultSet.next()) {
            bank.setId(resultSet.getLong("id"));
            bank.setName(resultSet.getString("name"));
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
