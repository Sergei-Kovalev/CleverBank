package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Bank;

import java.sql.*;

public class BankDAOImpl implements BankDAO {
    private final static String FIND_BY_ID = "SELECT * FROM banks WHERE id = ?";
    private final static String FIND_BY_NAME = "SELECT * FROM banks WHERE name = ?";
    private final static String SAVE_NEW_BANK = """
            INSERT INTO banks (name)
            VALUES
                (?);
            """;
    private final static String UPDATE_BANK = """
            UPDATE banks
            SET name = ?
            WHERE id = ?;
            """;
    private final static String DELETE_BANK_BY_ID = "DELETE FROM banks WHERE id = ?";
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

    private void fillBankData(ResultSet resultSet, Bank bank) throws SQLException {
        bank.setId(resultSet.getLong("id"));
        bank.setName(resultSet.getString("name"));
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
