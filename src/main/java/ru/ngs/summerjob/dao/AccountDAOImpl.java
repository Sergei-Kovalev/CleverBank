package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Класс выполняющий роль dao
 * и имплементирующий интерфейс:
 * @see AccountDAO
 */
public class AccountDAOImpl implements AccountDAO {
    /**
     * Константа для SQL запроса поиска счёта по id.
     */
    private final static String FIND_BY_ID = "SELECT * FROM accounts WHERE id = ?";
    /**
     * Константа для SQL запроса поиска счёта по имени.
     */
    private final static String FIND_BY_NAME = "SELECT * FROM accounts WHERE name = ?";
    /**
     * Константа для SQL запроса поиска счетов по id банка.
     */
    private final static String FIND_BY_BANK_ID = "SELECT * FROM accounts WHERE bank_id = ?";
    /**
     * Константа для SQL запроса поиска счетов по id клиента.
     */
    private final static String FIND_ALL_BY_USER_ID = "SELECT * FROM accounts WHERE user_id = ?";
    /**
     * Константа для SQL запроса сохранения нового счёта.
     */
    private final static String SAVE_NEW_ACCOUNT = """
            INSERT INTO accounts (name, opening_date, balance, user_id, bank_id, currency_id)
            VALUES
                (?, ?, ?, ?, ?, ?);
            """;
    /**
     * Константа для SQL запроса обновления данных счёта.
     */
    private final static String UPDATE_ACCOUNT = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?;
            """;
    /**
     * Константа для SQL запроса удаления счёта по id.
     */
    private final static String DELETE_ACCOUNT_BY_ID = "DELETE FROM accounts WHERE id = ?";

    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see UserDAO
     */
    UserDAO userDAO;
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see BankDAO
     */
    BankDAO bankDAO;
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see CurrencyDAO
     */
    CurrencyDAO currencyDAO;

    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов.
     */
    public AccountDAOImpl() {
        this.userDAO = new UserDAOImpl();
        this.bankDAO = new BankDAOImpl();
        this.currencyDAO = new CurrencyDAOImpl();
    }
    /**
     * Имплементация метода получения счёта по id.
     * @see Account
     * @param id - id счёта.
     * @return возвращает объект счёта.
     */
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
    /**
     * Имплементация сохранения счёта.
     * @see Account
     * @param account - принимает объект счёта.
     * @return объект сохраненного счёта.
     */
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
    /**
     * Имплементация метода обновления данных счёта.
     * @see Account
     * @param account - объект счёта для изменения.
     * @return возвращает изменённый объкт счёта.
     */
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
    /**
     * Имплементация удаления счёта по id.
     * @param id - id счёта для удаления из БД.
     * @return возвращает сообщение об успешном удалении.
     */
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
    /**
     * Имплементация метода получения списка счётов по id пользователя.
     * @see Account
     * @param userId - id пользователя.
     * @return возвращает список счетов.
     */
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
    /**
     * Имплементация метода получения счёта по имени.
     * @see Account
     * @param accountName - имя счёта.
     * @return возвращает объект счёта.
     */
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
    /**
     * Имплементация метода получения списка счетов по id банка.
     * @see Account
     * @param bankId - id банка.
     * @return возвращает список счетов открытых в банке.
     */
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
    /**
     * Вспомогательный метод для заполнения данных объекта счёта.
     * @param resultSet - список результатов полученных по SQL запросу.
     * @param account - Объект счёта для заполнения.
     * @throws SQLException - ошибка SQL запроса
     */
    private void fillAccountData(ResultSet resultSet, Account account) throws SQLException {
        account.setId(resultSet.getLong("id"));
        account.setName(resultSet.getString("name"));
        account.setOpeningDate(resultSet.getTimestamp("opening_date").toLocalDateTime());
        account.setBalance(resultSet.getDouble("balance"));
        account.setUser(userDAO.getUserById(resultSet.getLong("user_id")));
        account.setBank(bankDAO.getBankById(resultSet.getLong("bank_id")));
        account.setCurrency(currencyDAO.getCurrencyById(resultSet.getLong("currency_id")));
    }

    /**
     * Вспомогательный метод для получения подключения к базе данных.
     * @return возращает объект для подключения к БД.
     * @throws SQLException - ошибка SQL запроса.
     */
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
