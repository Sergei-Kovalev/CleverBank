package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see TransactionServiceImpl
 */
public interface TransactionService {
    /**
     * Метод для получения транзакции по id.
     * @see Transaction
     * @param id - id транзакции.
     * @return объект транзакции.
     */
    Transaction getTransactionById(long id);
    /**
     * Метод получения суммы приходных операций по счёту.
     * @param account - счёт клиента.
     * @param fromDate - дата начала операций.
     * @param toDate - дата окончания операций.
     * @return возвращает сумму операций.
     */
    double getTotalIncome(Account account, LocalDateTime fromDate, LocalDateTime toDate);
    /**
     * Метод получения суммы расходных операций по счёту.
     * @param account - счёт клиента.
     * @param fromDate - дата начала операций.
     * @param toDate - дата окончания операций.
     * @return возвращает сумму операций.
     */
    double getTotalOutcome(Account account, LocalDateTime fromDate, LocalDateTime toDate);
    /**
     * Метод для сохранения транзакции (для сервлета).
     * @param transaction - принимает объект транзакции.
     * @return возвращает сохраненный объект транзакции.
     * @see Transaction
     */
    Transaction saveTransactionForServlet(Transaction transaction);
    /**
     * Метод для обновленя данных транзакции.
     * @see Transaction
     * @param transaction - принимает объект транзакции.
     * @return возвращает измененный объект транзакции.
     */
    Transaction updateTransaction(Transaction transaction);
    /**
     * Метод удаления транзакции по id.
     * @param id - принимает id транзакции для удаления.
     * @return возвращает строку об успешном удалении.
     */
    String deleteTransactionById(long id);
    /**
     * Метод для получения всех транзакций клиента за указанный период.
     * @param id - id клиента.
     * @param fromDate - дата начала транзакций.
     * @param toDate - дата завершения транзакций.
     * @return возвращает список транзакций клиента за период.
     */
    List<Transaction> getTransactionsByUserIdAndPeriod(long id, LocalDateTime fromDate, LocalDateTime toDate);
    /**
     * Метод для сохранения транзакции (для консольного приложения).
     * @param transaction - принимает объект транзакции.
     * @return true при успешном сохранении транзакции.
     * @see Transaction
     */
    boolean saveTransaction(Transaction transaction);
}
