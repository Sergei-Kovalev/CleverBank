package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.AccountDAO;
import ru.ngs.summerjob.dao.AccountDAOImpl;
import ru.ngs.summerjob.entity.Account;

import java.util.List;

/**
 * @author Sergey Kovalev
 * Реализация интерфейса и методов класса:
 * @see AccountService
 */
public class AccountServiceImpl implements AccountService {
    /**
     * Это поле для загрузки dao получающего необходимы данные из БД.
     * @see AccountDAO
     */
    AccountDAO accountDAO;
    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов.
     */
    public AccountServiceImpl() {
        this.accountDAO = new AccountDAOImpl();
    }
    /**
     * Имплементация метода возвращающего счёт по id.
     * @param id - id счёта.
     * @return - объект класса:
     * @see Account
     */
    @Override
    public Account getAccountById(long id) {
        return accountDAO.getAccountById(id);
    }
    /**
     * Имплементация метода сохраняющего объект счёта.
     * @see Account
     * @param account - принимает объект счёта.
     * @return возвращает объект счёта сохраненный в базе данных (с заполненным из БД id).
     */
    @Override
    public Account saveAccount(Account account) {
        return accountDAO.saveAccount(account);
    }
    /**
     * Имплементация метода обновления данных счёта.
     * @see Account
     * @param account - принимает объект счёта, который необходимо изменить.
     * @return возвращает объект счёта измененный в базе дынных (с заполненными из БД данными).
     */
    @Override
    public Account updateAccount(Account account) {
        return accountDAO.updateAccount(account);
    }
    /**
     * Имплементация метода удаления счёта по id.
     * @param id - параметр принимающий id счёта.
     * @return возвращает строку об успешном удалении счёта.
     */
    @Override
    public String deleteAccount(long id) {
        return accountDAO.deleteAccount(id);
    }

    /**
     * Имплементация метода получения счётов по id клиента.
     * @param userId - параметр принимающий id клиента.
     * @return возвращает список счетов отрытых клиентом.
     * @see Account
     */
    @Override
    public List<Account> getAccountsByUserId(long userId) {
        return accountDAO.getAccountsByUserId(userId);
    }
    /**
     * Имплементация метода получения счёта по его имени.
     * @param accountName - параметр принимающий имя счёта.
     * @return возвращает объект счета.
     * @see Account
     */
    @Override
    public Account getAccountByName(String accountName) {
        return accountDAO.getAccountByName(accountName);
    }
    /**
     * Имплементация метода получения счётов по id банка.
     * @param bankId - параметр принимающий id банка.
     * @return возвращает список счетов отрытых в определенном банке.
     * @see Account
     */
    @Override
    public List<Account> getAccountsByBankId(long bankId) {
        return accountDAO.getAccountByBankId(bankId);
    }
}
