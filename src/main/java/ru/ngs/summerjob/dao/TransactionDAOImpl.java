package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class TransactionDAOImpl implements TransactionDAO {
    private final static String FIND_BY_ID = "SELECT * FROM transactions WHERE id = ?";
    private final static String FIND_BY_USER_ID =
            "SELECT * FROM transactions WHERE sender_account_id = ? OR recipient_account_id = ?";
    private final static String SAVE_TRANSACTION = """
            INSERT INTO transactions(transaction_date, transaction_type_id, sender_account_id, recipient_account_id, amount)
            VALUES
                (?, ?, ?, ?, ?);""";

    private final static String APPEND_FOR_REPLENISHMENT_AND_WITHDRAW = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?;
            """;

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
            while (resultSet.next()) {
                fillTransaction(resultSet, transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }
    @Override
    public List<Transaction> getTransactionsByUserId(long id) {
        List<Transaction> transactions = new ArrayList<>();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID);
            statement.setLong(1, id);
            statement.setLong(2, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                fillTransaction(resultSet, transaction);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        try(Connection connection = getConnection()) {
            if (transaction.getType().getId() == 3) {
                PreparedStatement statement = connection.prepareStatement(
                        SAVE_TRANSACTION + APPEND_FOR_REPLENISHMENT_AND_WITHDRAW);
                fillConstantFieldsForSavingTransaction(transaction, statement);

                statement.setNull(3, NULL);

                statement.setDouble(6, transaction.getAccountRecipient().getBalance() + transaction.getAmount());
                statement.setLong(7, transaction.getAccountRecipient().getId());

                statement.executeUpdate();
            } else if (transaction.getType().getId() == 2) {
                PreparedStatement statement = connection.prepareStatement(
                        SAVE_TRANSACTION + APPEND_FOR_REPLENISHMENT_AND_WITHDRAW);
                fillConstantFieldsForSavingTransaction(transaction, statement);

                statement.setNull(3, NULL);

                statement.setDouble(6, transaction.getAccountRecipient().getBalance() - transaction.getAmount());
                statement.setLong(7, transaction.getAccountRecipient().getId());

                statement.executeUpdate();
            } else if (transaction.getType().getId() == 1) {
                PreparedStatement statement = connection.prepareStatement(
                        SAVE_TRANSACTION + APPEND_FOR_REPLENISHMENT_AND_WITHDRAW + APPEND_FOR_REPLENISHMENT_AND_WITHDRAW
                );
                fillConstantFieldsForSavingTransaction(transaction, statement);

                statement.setLong(3, transaction.getAccountSender().getId());

                statement.setDouble(6, transaction.getAccountSender().getBalance() - transaction.getAmount());
                statement.setLong(7, transaction.getAccountSender().getId());
                statement.setDouble(8, transaction.getAccountRecipient().getBalance() + transaction.getAmount());
                statement.setLong(9, transaction.getAccountRecipient().getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private static void fillConstantFieldsForSavingTransaction(Transaction transaction, PreparedStatement statement) throws SQLException {
        statement.setTimestamp(1, Timestamp.valueOf(transaction.getDate()));
        statement.setLong(2, transaction.getType().getId());
        statement.setLong(4, transaction.getAccountRecipient().getId());
        statement.setDouble(5, transaction.getAmount());
    }

    private void fillTransaction(ResultSet resultSet, Transaction transaction) throws SQLException {
        transaction.setId(resultSet.getLong("id"));
        transaction.setDate(resultSet.getTimestamp("transaction_date").toLocalDateTime());
        transaction.setType(transactionTypeDAO.getTransactionTypeById(
                resultSet.getLong("transaction_type_id")));
        transaction.setAccountSender(accountDAO.getAccountById(
                resultSet.getLong("sender_account_id")));
        transaction.setAccountRecipient(accountDAO.getAccountById(
                resultSet.getLong("recipient_account_id")));
        transaction.setAmount(resultSet.getDouble("amount"));
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Config.getConfig().get("db").get("url"),
                Config.getConfig().get("db").get("login"),
                Config.getConfig().get("db").get("password")
        );
    }
}
