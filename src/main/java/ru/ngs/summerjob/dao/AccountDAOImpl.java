package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Account;

import java.sql.*;

public class AccountDAOImpl implements AccountDAO {
    private final static String FIND_BY_ID = "SELECT * FROM accounts WHERE id = ?";
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
            fillAccountData(resultSet, account);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    private void fillAccountData(ResultSet resultSet, Account account) throws SQLException {
        while (resultSet.next()) {
            account.setId(resultSet.getLong("id"));
            account.setName(resultSet.getString("name"));
            account.setOpeningDate(resultSet.getTimestamp("opening_date").toLocalDateTime());
            account.setBalance(resultSet.getLong("balance"));
            account.setUser(userDAO.getUserById(resultSet.getLong("user_id")));
            account.setBank(bankDAO.getBankById(resultSet.getLong("bank_id")));
            account.setCurrency(currencyDAO.getCurrencyById(resultSet.getLong("currency_id")));
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