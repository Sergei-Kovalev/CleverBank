package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class TransactionDAOImpl implements TransactionDAO {
    private final static String FIND_BY_ID = "SELECT * FROM transactions WHERE id = ?";
    private final static String SAVE_TRANSACTION = """
            INSERT INTO transactions(transaction_date, transaction_type_id, sender_account_id, recipient_account_id, amount)
            VALUES
                (?, ?, ?, ?, ?);""";
    private final static String UPDATE_TRANSACTION_AMOUNT = """
            UPDATE transactions
            SET amount = ?
            WHERE id = ?;
            """;
    private final static String DELETE_TRANSACTION_BY_ID = "DELETE FROM transactions WHERE id = ?";

    private final static String FIND_BY_USER_ID_AND_PERIOD = """
            SELECT * FROM transactions
            WHERE (transaction_date BETWEEN ? AND ?)
            AND (sender_account_id = ? OR recipient_account_id = ?);
            """;
    private final static String APPEND_FOR_REPLENISHMENT_AND_WITHDRAW = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?;
            """;
    private final static String SUM_FROM_INCOME = """
           SELECT SUM(amount) AS sum FROM transactions
            WHERE (transaction_date BETWEEN ? AND ?)
            AND (recipient_account_id = ?)
            AND (transaction_type_id = 1 OR transaction_type_id = 3 OR transaction_type_id = 4);
            """;
    private final static String SUM_FROM_OUTCOME = """
           SELECT SUM(amount) AS sum FROM transactions
            WHERE (transaction_date BETWEEN ? AND ?)
            AND (recipient_account_id = ? AND transaction_type_id = 2 OR transaction_type_id = 5)
            OR (sender_account_id = ?);
            """;

    TransactionTypeDAO transactionTypeDAO;
    AccountDAO accountDAO;


    public TransactionDAOImpl() {
        this.transactionTypeDAO = new TransactionTypeDAOImpl();
        this.accountDAO = new AccountDAOImpl();
    }

    public double getTotalIncome(Account account, LocalDateTime fromDate, LocalDateTime toDate) {
        double income = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SUM_FROM_INCOME);
            statement.setTimestamp(1, Timestamp.valueOf(fromDate));
            statement.setTimestamp(2, Timestamp.valueOf(toDate));
            statement.setLong(3, account.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                income = resultSet.getDouble("sum");
            }
            return income;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public double getTotalOutcome(Account account, LocalDateTime fromDate, LocalDateTime toDate) {
        double outcome = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SUM_FROM_OUTCOME);
            statement.setTimestamp(1, Timestamp.valueOf(fromDate));
            statement.setTimestamp(2, Timestamp.valueOf(toDate));
            statement.setLong(3, account.getId());
            statement.setLong(4, account.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                outcome = resultSet.getDouble("sum");
            }
            return -outcome;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public Transaction saveTransactionForServlet(Transaction transaction) {
        long id = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.valueOf(transaction.getDate()));
            statement.setLong(2, transaction.getType().getId());
            if (transaction.getAccountSender().getId() == 0) {
                statement.setNull(3, NULL);
            } else {
                statement.setLong(3, transaction.getAccountSender().getId());
            }
            statement.setLong(4, transaction.getAccountRecipient().getId());
            statement.setDouble(5, transaction.getAmount());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            transaction.setId(id);
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION_AMOUNT);
            statement.setDouble(1, transaction.getAmount());
            statement.setLong(2, transaction.getId());
            statement.executeUpdate();
            return getTransactionById(transaction.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteTransactionById(long id) {
        try(Connection connection = getConnection()) {
            Transaction transaction = getTransactionById(id);
            if (transaction.getId() == 0) {
                return "Транзакций с таким id нет в базе данных";
            } else {
                PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION_BY_ID);
                statement.setLong(1, id);
                statement.executeUpdate();
                return "Транзакция с id " + id + " удалена.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Transaction> getTransactionsByUserIdAndPeriod(long userId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Transaction> transactions = new ArrayList<>();
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID_AND_PERIOD);
            statement.setTimestamp(1, Timestamp.valueOf(fromDate));
            statement.setTimestamp(2, Timestamp.valueOf(toDate));
            statement.setLong(3, userId);
            statement.setLong(4, userId);
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
            if (transaction.getType().getId() == 3 || transaction.getType().getId() == 4) {
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
