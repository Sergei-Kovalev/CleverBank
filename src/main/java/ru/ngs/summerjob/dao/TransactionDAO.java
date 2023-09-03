package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see TransactionDAO
 */
public interface TransactionDAO {
    /**
     * Метод для получения транзакции по id.
     * @see Transaction
     * @param id - id транзакции.
     * @return объект транзакции.
     */
    Transaction getTransactionById(long id);

    /**
     * Метод получения суммы приходных транзакций из БД за период.
     * @param account - счёт клиента.
     * @param fromDate - дата начала транзакций.
     * @param toDate - дата окончания транзакций.
     * @return сумма приходных операций по счёту.
     */
    double getTotalIncome(Account account, LocalDateTime fromDate, LocalDateTime toDate);
    /**
     * Метод получения суммы расходных транзакций из БД за период.
     * @param account - счёт клиента.
     * @param fromDate - дата начала транзакций.
     * @param toDate - дата окончания транзакций.
     * @return сумма расходных операций по счёту.
     */
    double getTotalOutcome(Account account, LocalDateTime fromDate, LocalDateTime toDate);
    /**
     * Метод для сохранения транзакции (для сервлета).
     * @see Transaction
     * @param transaction - объект транзакции.
     * @return объект сохраненной транзакции.
     */
    Transaction saveTransactionForServlet(Transaction transaction);
    /**
     * Метод для обновления данных транзакции.
     * @see Transaction
     * @param transaction - объект транзакции.
     * @return объект измененной транзакции.
     */
    Transaction updateTransaction(Transaction transaction);
    /**
     * Метод для удаления транзакции из БД.
     * @see Transaction
     * @param id - id транзакции.
     * @return сообщение об удалении транзакции.
     */
    String deleteTransactionById(long id);
    /**
     * Метод для получения списка транзакций клиента за период.
     * @param userId - id клиента.
     * @param fromDate - дата начала транзакций.
     * @param toDate - дата окончания транзакций.
     * @return список транзакций клиента за период.
     */
    List<Transaction> getTransactionsByUserIdAndPeriod(long userId, LocalDateTime fromDate, LocalDateTime toDate);
    /**
     * Метод для сохранения транзакции (для консольного приложения).
     * @see Transaction
     * @param transaction - объект транзакции.
     * @return true если сохранение прошло успешно.
     */
    boolean saveTransaction(Transaction transaction);
}
