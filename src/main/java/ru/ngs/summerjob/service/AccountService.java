package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Account;

import java.util.List;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see AccountServiceImpl
 */
public interface AccountService {
    /**
     * Метод для получения счёта по id.
     * @see Account
     * @param id - id счёта.
     * @return объект счёта.
     */
    Account getAccountById(long id);
    /**
     * Метод для сохранения счёта.
     * @param account - принимает объект счёта.
     * @return возвращает сохраненный объект счёта.
     * @see Account
     */
    Account saveAccount(Account account);
    /**
     * Метод для обновленя данных счёта.
     * @see Account
     * @param account - принимает объект счёта.
     * @return возвращает измененный объект счёта.
     */
    Account updateAccount(Account account);
    /**
     * Метод удаления счёта по id.
     * @param id - принимает id счёта для удаления.
     * @return возвращает строку об успешном удалении.
     */
    String deleteAccount(long id);
    /**
     * Метод получения списка счетов открытых клиентом.
     * @param userId - id клиента.
     * @return список счетов клиента.
     * @see Account
     */
    List<Account> getAccountsByUserId(long userId);

    /**
     * Метод возвращающий объект счета по его имени.
     * @param accountName - имя счёта.
     * @return объект счёта.
     * @see Account
     */
    Account getAccountByName(String accountName);
    /**
     * Метода получения списка счетов открытых в опеределенном банке.
     * @param bankId - id банка.
     * @return список счетов открытых в банке.
     * @see Account
     */
    List<Account> getAccountsByBankId(long bankId);
}
