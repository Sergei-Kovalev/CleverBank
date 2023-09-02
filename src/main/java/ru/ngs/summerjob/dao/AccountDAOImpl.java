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
    private final static String SAVE_NEW_ACCOUNT = """
            INSERT INTO accounts (name, opening_date, balance, user_id, bank_id, currency_id)
            VALUES
                (?, ?, ?, ?, ?, ?);
            """;
    private final static String UPDATE_ACCOUNT = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?;
            """;
    private final static String DELETE_ACCOUNT_BY_ID = "DELETE FROM accounts WHERE id = ?";
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
    public Account saveAccount(Account account) {
        long id = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_NEW_ACCOUNT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getName());
            statement.setTimestamp(2, Timestamp.valueOf(account.getOpeningDate()));
            statement.setDouble(3, account.getBalance());
            statement.setLong(4, account.getUser().getId());
            statement.setLong(5, account.getBank().getId());
            statement.setLong(6, account.getCurrency().getId());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            account.setId(id);
            return account;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account updateAccount(Account account) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT);
            statement.setDouble(1, account.getBalance());
            statement.setLong(2, account.getId());
            statement.executeUpdate();
            return getAccountById(account.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteAccount(long id) {
        try(Connection connection = getConnection()) {
            Account account = getAccountById(id);
            if (account.getId() == 0) {
                return "Клиента с таким id нет в базе данных";
            } else {
                PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT_BY_ID);
                statement.setLong(1, id);
                statement.executeUpdate();
                return "Клиент с id " + id + " удален.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
