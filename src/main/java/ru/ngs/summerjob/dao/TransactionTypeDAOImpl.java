package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.TransactionType;

import java.sql.*;

public class TransactionTypeDAOImpl implements TransactionTypeDAO {

    private final static String FIND_BY_ID = "SELECT * FROM transaction_types WHERE id = ?";

    @Override
    public TransactionType getTransactionTypeById(long id) {
        TransactionType transactionType = new TransactionType();
        try (Connection connection = DriverManager.getConnection(
                Config.getConfig().get("db").get("url"),
                Config.getConfig().get("db").get("login"),
                Config.getConfig().get("db").get("password")
        )) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactionType.setId(resultSet.getLong("id"));
                transactionType.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactionType;
    }
}
