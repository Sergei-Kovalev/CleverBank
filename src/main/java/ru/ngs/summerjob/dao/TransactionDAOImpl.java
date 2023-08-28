package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {
    private final static String FIND_BY_ID = "SELECT * FROM transactions WHERE id = ?";
    private final static String FIND_BY_USER_ID =
            "SELECT * FROM transactions WHERE sender_account_id = ? OR recipient_account_id = ?";
    TransactionTypeDAO transactionTypeDAO;
    AccountDAO accountDAO;

    public TransactionDAOImpl() {
        this.transactionTypeDAO = new TransactionTypeDAOImpl();
        this.accountDAO = new AccountDAOImpl();
    }

    @Override
    public Transaction getTransactionById(long id) {
        Transaction transaction = new Transaction();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            fillTransaction(resultSet, transaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }

    //TODO проверить работоспособность
    @Override
    public List<Transaction> getTransactionsByUserId(long id) {
        List<Transaction> transactions = new ArrayList<>();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID);
            statement.setLong(1, id);
            statement.setLong(2, id);
            ResultSet resultSet = statement.executeQuery();
            fillAllTransactions(resultSet, transactions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    private void fillAllTransactions(ResultSet resultSet, List<Transaction> transactions) throws SQLException {
        while (resultSet.next()) {
            Transaction transaction = new Transaction();
            transaction.setId(resultSet.getLong("id"));
            transaction.setDate(resultSet.getTimestamp("transaction_date").toLocalDateTime());
            transaction.setType(transactionTypeDAO.getTransactionTypeById(
                    resultSet.getLong("transaction_type_id")));
            transaction.setAccountSender(accountDAO.getAccountById(
                    resultSet.getLong("sender_account_id")));
            transaction.setAccountRecipient(accountDAO.getAccountById(
                    resultSet.getLong("recipient_account_id")));
            transaction.setAmount(resultSet.getLong("amount"));
            transactions.add(transaction);
        }
    }

    private void fillTransaction(ResultSet resultSet, Transaction transaction) throws SQLException {
        while (resultSet.next()) {
            transaction.setId(resultSet.getLong("id"));
            transaction.setDate(resultSet.getTimestamp("transaction_date").toLocalDateTime());
            transaction.setType(transactionTypeDAO.getTransactionTypeById(
                    resultSet.getLong("transaction_type_id")));
            transaction.setAccountSender(accountDAO.getAccountById(
                    resultSet.getLong("sender_account_id")));
            transaction.setAccountRecipient(accountDAO.getAccountById(
                    resultSet.getLong("recipient_account_id")));
            transaction.setAmount(resultSet.getLong("amount"));
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
