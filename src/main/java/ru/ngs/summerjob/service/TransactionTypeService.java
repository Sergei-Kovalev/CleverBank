package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.TransactionType;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see TransactionTypeServiceImpl
 */
public interface TransactionTypeService {
    /**
     * Метод для получения типа транзакции по id.
     * @see TransactionType
     * @param id - id типа транзакции.
     * @return объект типа транзакции.
     */
    TransactionType getTransactionTypeById(long id);
    /**
     * Метод для сохранения типа транзакции.
     * @param transactionType - принимает объект типа транзакции.
     * @return возвращает сохраненный объект типа транзакции.
     * @see TransactionType
     */
    TransactionType saveTransactionType(TransactionType transactionType);
    /**
     * Метод для обновленя данных типа транзакции.
     * @param transactionType - принимает объект типа транзакции.
     * @return возвращает измененный объект типа транзакции.
     * @see TransactionType
     */
    TransactionType updateTransactionType(TransactionType transactionType);
    /**
     * Метод удаления типа транзакции по id.
     * @param id - принимает id типа транзакции для удаления.
     * @return возвращает строку об успешном удалении.
     */
    String deleteTransactionTypeById(long id);
}
