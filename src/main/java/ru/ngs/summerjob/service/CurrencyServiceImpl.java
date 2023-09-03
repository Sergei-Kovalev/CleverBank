package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.CurrencyDAO;
import ru.ngs.summerjob.dao.CurrencyDAOImpl;
import ru.ngs.summerjob.entity.Currency;

/**
 * @author Sergey Kovalev
 * Реализация интерфейса и методов класса:
 * @see CurrencyService
 */
public class CurrencyServiceImpl implements CurrencyService {
    /**
     * Это поле для загрузки dao получающего необходимы данные из БД.
     * @see CurrencyDAO
     */
    CurrencyDAO currencyDAO;
    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов.
     */
    public CurrencyServiceImpl() {
        this.currencyDAO = new CurrencyDAOImpl();
    }
    /**
     * Имплементация метода возвращающего валюту по id.
     * @param id - id валюты.
     * @return - объект класса:
     * @see Currency
     */
    @Override
    public Currency getCurrencyById(long id) {
        return currencyDAO.getCurrencyById(id);
    }
    /**
     * Имплементация метода сохраняющего объект валюта.
     * @see Currency
     * @param currency - принимает объект валюты.
     * @return возвращает объект валюты сохраненный в базе данных (с заполненным из БД id).
     */
    @Override
    public Currency saveCurrency(Currency currency) {
        return currencyDAO.saveCurrency(currency);
    }
    /**
     * Имплементация метода обновления данных валюты.
     * @see Currency
     * @param currency - принимает объект валюты, который необходимо изменить.
     * @return возвращает объект валюты измененный в базе дынных (с заполненными из БД данными).
     */
    @Override
    public Currency updateCurrency(Currency currency) {
        return currencyDAO.updateCurrency(currency);
    }
    /**
     * Имплементация метода удаления валюты по id.
     * @param id - параметр принимающий id валюты.
     * @return возвращает строку об успешном удалении валюты.
     */
    @Override
    public String deleteCurrency(long id) {
        return currencyDAO.deleteCurrency(id);
    }

}
