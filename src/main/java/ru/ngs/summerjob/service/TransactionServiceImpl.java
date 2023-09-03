package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.TransactionDAO;
import ru.ngs.summerjob.dao.TransactionDAOImpl;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Реализация интерфейса и методов класса:
 * @see TransactionService
 */
public class TransactionServiceImpl implements TransactionService {
    /**
     * Это поле для загрузки dao получающего необходимы данные из БД.
     * @see TransactionDAO
     */
    TransactionDAO transactionDAO;

    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов.
     */
    public TransactionServiceImpl() {
        this.transactionDAO = new TransactionDAOImpl();
    }
    /**
     * Имплементация метода возвращающего транзакцию по id.
     * @param id - id транзакции.
     * @return - объект класса:
     * @see Transaction
     */
    @Override
    public Transaction getTransactionById(long id) {
        return transactionDAO.getTransactionById(id);
    }

    /**
     * Имплементация метода возвращающего общую сумму приходных операций.
     * @param account - счёт по которому будет расчёт.
     * @param fromDate - дата начала операций.
     * @param toDate - дата окончания операций.
     * @return - сумма пополнений счёта.
     */
    @Override
    public double getTotalIncome(Account account, LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionDAO.getTotalIncome(account, fromDate, toDate);
    }

    /**
     * Имплементация метода возвращающего общую сумму расходных операций.
     * @param account - счёт по которому будет расчёт.
     * @param fromDate - дата начала операций.
     * @param toDate - дата окончания операций.
     * @return - сумма изъятий со счёта.
     */
    public double getTotalOutcome(Account account, LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionDAO.getTotalOutcome(account, fromDate, toDate);
    }
    /**
     * Имплементация метода сохраняющего объект транзакция (для сервлетов).
     * @see Transaction
     * @param transaction - принимает объект транзакции.
     * @return возвращает объект транзакции сохраненный в базе данных (с заполненным из БД id).
     */
    @Override
    public Transaction saveTransactionForServlet(Transaction transaction) {
        return transactionDAO.saveTransactionForServlet(transaction);
    }
    /**
     * Имплементация метода обновления данных транзакции.
     * @see Transaction
     * @param transaction - принимает объект транзакции, который необходимо изменить.
     * @return возвращает объект транзакции измененного в базе дынных (с заполненными из БД данными).
     */
    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionDAO.updateTransaction(transaction);
    }
    /**
     * Имплементация метода удаления транзакции по id.
     * @param id - параметр принимающий id транзакции.
     * @return возвращает строку об успешном удалении транзакции.
     */
    @Override
    public String deleteTransactionById(long id) {
        return transactionDAO.deleteTransactionById(id);
    }

    /**
     * Имплементация метода получения транзакций по id клиента за указанный период.
     * @param id - id клиента
     * @param fromDate - дата начала периода.
     * @param toDate - ддата окончания периода.
     * @return список траназакций.
     */
    @Override
    public List<Transaction> getTransactionsByUserIdAndPeriod(long id, LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionDAO.getTransactionsByUserIdAndPeriod(id, fromDate, toDate);
    }
    /**
     * Имплементация метода сохраняющего объект транзакция (для десктопного приложения).
     * @see Transaction
     * @param transaction - принимает объект транзакции.
     * @return true если сохранение транзакции прошло успешно.
     */
    @Override
    public boolean saveTransaction(Transaction transaction) {
        return transactionDAO.saveTransaction(transaction);
    }
}
