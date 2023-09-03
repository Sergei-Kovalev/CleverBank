package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Currency;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see CurrencyDAOImpl
 */
public interface CurrencyDAO {
    /**
     * Метод для получения валюты по id.
     * @see Currency
     * @param id - id валюты.
     * @return объект валюты.
     */
    Currency getCurrencyById(long id);
    /**
     * Метод для сохранения валюты.
     * @see Currency
     * @param currency - объект валюты.
     * @return объект сохраненной валюты.
     */
    Currency saveCurrency(Currency currency);
    /**
     * Метод для обновления данных валюты.
     * @see Currency
     * @param currency - объект валюты.
     * @return объект измененной валюты.
     */
    Currency updateCurrency(Currency currency);
    /**
     * Метод для удаления валюты из БД.
     * @see Currency
     * @param id - id валюты.
     * @return сообщение об удалении валюты.
     */
    String deleteCurrency(long id);
}
