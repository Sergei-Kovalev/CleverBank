package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {
    private final static String FIND_BY_ID = "SELECT * FROM accounts WHERE id = ?";
    private final static String FIND_BY_NAME = "SELECT * FROM accounts WHERE name = ?";
    private final static String FIND_BY_BANK_ID = "SELECT * FROM accounts WHERE bank_id = ?";
    private final static String FIND_ALL_BY_USER_ID = "SELECT * FROM accounts WHERE user_id = ?";
    UserDAO userDAO;
    BankDAO bankDAO;
    CurrencyDAO currencyDAO;

    public AccountDAOImpl() {
        this.userDAO = new UserDAOImpl();
        this.bankDAO = new BankDAOImpl();
        this.currencyDAO = new CurrencyDAOImpl();
    }

    @Override
    public Account getAccountById(long id) {
        Account account = new Account();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                fillAccountData(resultSet, account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    @Override
    public List<Account> getAccountsByUserId(long userId) {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                fillAccountData(resultSet, account);
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    @Override
    public Account getAccountByName(String accountName) {
        Account account = new Account();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME);
            statement.setString(1, accountName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                fillAccountData(resultSet, account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    @Override
    public List<Account> getAccountByBankId(long bankId) {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_BANK_ID);
            statement.setLong(1, bankId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                fillAccountData(resultSet, account);
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    private void fillAccountData(ResultSet resultSet, Account account) throws SQLException {
        account.setId(resultSet.getLong("id"));
        account.setName(resultSet.getString("name"));
        account.setOpeningDate(resultSet.getTimestamp("opening_date").toLocalDateTime());
        account.setBalance(resultSet.getDouble("balance"));
        account.setUser(userDAO.getUserById(resultSet.getLong("user_id")));
        account.setBank(bankDAO.getBankById(resultSet.getLong("bank_id")));
        account.setCurrency(currencyDAO.getCurrencyById(resultSet.getLong("currency_id")));
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Config.getConfig().get("db").get("url"),
                Config.getConfig().get("db").get("login"),
                Config.getConfig().get("db").get("password")
        );
    }
}
