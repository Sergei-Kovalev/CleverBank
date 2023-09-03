package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Currency;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see CurrencyServiceImpl
 */
public interface CurrencyService {
    /**
     * Метод для получения валюты по id.
     * @see Currency
     * @param id - id валюты.
     * @return объект валюты.
     */
    Currency getCurrencyById(long id);
    /**
     * Метод для сохранения валюты.
     * @param currency - принимает объект валюты.
     * @return возвращает сохраненный объект валюты.
     * @see Currency
     */
    Currency saveCurrency(Currency currency);
    /**
     * Метод для обновленя данных валюты.
     * @see Currency
     * @param currency - принимает объект валюты.
     * @return возвращает измененный объект валюты.
     */
    Currency updateCurrency(Currency currency);
    /**
     * Метод удаления валюты по id.
     * @param id - принимает id валюты для удаления.
     * @return возвращает строку об успешном удалении.
     */
    String deleteCurrency(long id);
}
