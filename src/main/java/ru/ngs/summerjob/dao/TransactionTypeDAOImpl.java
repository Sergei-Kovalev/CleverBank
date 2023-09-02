package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.TransactionType;

import java.sql.*;

public class TransactionTypeDAOImpl implements TransactionTypeDAO {

    private final static String FIND_BY_ID = "SELECT * FROM transaction_types WHERE id = ?";
    private final static String SAVE_NEW_TRANSACTION_TYPE = """
            INSERT INTO transaction_types (name)
            VALUES
                (?);
            """;
    private final static String UPDATE_TRANSACTION_TYPES = """
            UPDATE transaction_types
            SET name = ?
            WHERE id = ?;
            """;
    private final static String DELETE_TRANSACTION_TYPE_BY_ID = "DELETE FROM transaction_types WHERE id = ?";

    @Override
    public TransactionType getTransactionTypeById(long id) {
        TransactionType transactionType = new TransactionType();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            fillTransactionTypeData(resultSet, transactionType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactionType;
    }

    @Override
    public TransactionType saveTransactionType(TransactionType transactionType) {
        long id = 0;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_NEW_TRANSACTION_TYPE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, transactionType.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            transactionType.setId(id);
            return transactionType;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransactionType updateTransactionType(TransactionType transactionType) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION_TYPES);
            statement.setString(1, transactionType.getName());
            statement.setLong(2, transactionType.getId());
            statement.executeUpdate();
            return getTransactionTypeById(transactionType.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteTransactionTypeById(long id) {
        try(Connection connection = getConnection()) {
            TransactionType transactionType = getTransactionTypeById(id);
            if (transactionType.getId() == 0) {
                return "Вида транзакций с таким id нет в базе данных";
            } else {
                PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION_TYPE_BY_ID);
                statement.setLong(1, id);
                statement.executeUpdate();
                return "Вид транзакций с id " + id + " удален.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fillTransactionTypeData(ResultSet resultSet, TransactionType transactionType) throws SQLException {
        while (resultSet.next()) {
            transactionType.setId(resultSet.getLong("id"));
            transactionType.setName(resultSet.getString("name"));
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
