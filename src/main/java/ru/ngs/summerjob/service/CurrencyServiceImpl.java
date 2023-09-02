package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.CurrencyDAO;
import ru.ngs.summerjob.dao.CurrencyDAOImpl;
import ru.ngs.summerjob.entity.Currency;

public class CurrencyServiceImpl implements CurrencyService {
    CurrencyDAO currencyDAO;

    public CurrencyServiceImpl() {
        this.currencyDAO = new CurrencyDAOImpl();
    }

    @Override
    public Currency getCurrencyById(long id) {
        return currencyDAO.getCurrencyById(id);
    }

    @Override
    public Currency saveCurrency(Currency currency) {
        return currencyDAO.saveCurrency(currency);
    }

    @Override
    public Currency updateCurrency(Currency currency) {
        return currencyDAO.updateCurrency(currency);
    }

    @Override
    public String deleteCurrency(long id) {
        return currencyDAO.deleteCurrency(id);
    }

}
