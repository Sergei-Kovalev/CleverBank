package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.TransactionType;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see TransactionTypeDAO
 */
public interface TransactionTypeDAO {
    /**
     * Метод для получения типа транзакции по id.
     * @see TransactionType
     * @param id - id типа транзакции.
     * @return объект типа транзакции.
     */
    TransactionType getTransactionTypeById(long id);
    /**
     * Метод для сохранения типа транзакции.
     * @see TransactionType
     * @param transactionType - объект типа транзакции.
     * @return объект сохраненного типа транзакции.
     */
    TransactionType saveTransactionType(TransactionType transactionType);
    /**
     * Метод для обновления данных типа транзакции.
     * @see TransactionType
     * @param transactionType - объект типа транзакции.
     * @return объект измененного типа транзакции.
     */
    TransactionType updateTransactionType(TransactionType transactionType);
    /**
     * Метод для удаления типа транзакции из БД.
     * @see TransactionType
     * @param id - id типа транзакции.
     * @return сообщение об удалении типа транзакции.
     */
    String deleteTransactionTypeById(long id);
}
