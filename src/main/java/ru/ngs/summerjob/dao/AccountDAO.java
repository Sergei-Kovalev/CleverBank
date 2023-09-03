package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Account;

import java.util.List;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see AccountDAOImpl
 */
public interface AccountDAO {
    /**
     * Метод для получения счёта по id.
     * @see Account
     * @param id - id счёта.
     * @return объект счёта.
     */
    Account getAccountById(long id);
    /**
     * Метод для сохранения счёта.
     * @see Account
     * @param account - объект счёта.
     * @return объект сохраненного счёта.
     */
    Account saveAccount(Account account);
    /**
     * Метод для обновления данных счёта.
     * @see Account
     * @param account - объект счёта.
     * @return объект измененного счёта.
     */
    Account updateAccount(Account account);
    /**
     * Метод для удаления счёта из БД.
     * @see Account
     * @param id - id счёта.
     * @return сообщение об удалении счёта.
     */
    String deleteAccount(long id);

    /**
     * Метод для получения всех счетов клиента по его id.
     * @see Account
     * @param userId - id клиента.
     * @return список счетов клиента.
     */
    List<Account> getAccountsByUserId(long userId);

    /**
     * Метод для получения счета по имени.
     * @see Account
     * @param accountName - имя счёта.
     * @return объект счёта.
     */
    Account getAccountByName(String accountName);
    /**
     * Метод для получения всех счетов принадлежащих банку по его id.
     * @see Account
     * @param bankId - id банка.
     * @return список счетов банка.
     */
    List<Account> getAccountByBankId(long bankId);
}
