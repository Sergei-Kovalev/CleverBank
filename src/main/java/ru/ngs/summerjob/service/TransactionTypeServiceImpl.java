package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.TransactionTypeDAO;
import ru.ngs.summerjob.dao.TransactionTypeDAOImpl;
import ru.ngs.summerjob.entity.TransactionType;

/**
 * @author Sergey Kovalev
 * Реализация интерфейса и методов класса:
 * @see TransactionTypeService
 */
public class TransactionTypeServiceImpl implements TransactionTypeService {
    /**
     * Это поле для загрузки dao получающего необходимы данные из БД.
     * @see TransactionTypeDAO
     */
    TransactionTypeDAO transactionTypeDAO;
    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов.
     */
    public TransactionTypeServiceImpl() {
        this.transactionTypeDAO = new TransactionTypeDAOImpl();
    }
    /**
     * Имплементация метода возвращающего тип транзакции по id.
     * @param id - id типа транзакции.
     * @return - объект класса:
     * @see TransactionType
     */
    @Override
    public TransactionType getTransactionTypeById(long id) {
        return transactionTypeDAO.getTransactionTypeById(id);
    }
    /**
     * Имплементация метода сохраняющего объект тип транзакции.
     * @see TransactionType
     * @param transactionType - принимает объект типа транзакции.
     * @return возвращает объект тип транзакции сохраненный в базе данных (с заполненным из БД id).
     */
    @Override
    public TransactionType saveTransactionType(TransactionType transactionType) {
        return transactionTypeDAO.saveTransactionType(transactionType);
    }
    /**
     * Имплементация метода обновления данных типа транзакции.
     * @see TransactionType
     * @param transactionType - принимает объект типа транзакции, который необходимо изменить.
     * @return возвращает объект типа транзакции измененного в базе дынных (с заполненными из БД данными).
     */
    @Override
    public TransactionType updateTransactionType(TransactionType transactionType) {
        return transactionTypeDAO.updateTransactionType(transactionType);
    }
    /**
     * Имплементация метода удаления типа транзакции по id.
     * @param id - параметр принимающий id типа транзакции.
     * @return возвращает строку об успешном удалении типа транзакции.
     */
    @Override
    public String deleteTransactionTypeById(long id) {
        return transactionTypeDAO.deleteTransactionTypeById(id);
    }

}
