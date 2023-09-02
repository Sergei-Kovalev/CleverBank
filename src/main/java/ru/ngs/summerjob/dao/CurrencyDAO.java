package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Currency;

public interface CurrencyDAO {
    Currency getCurrencyById(long id);

    Currency saveCurrency(Currency currency);

    Currency updateCurrency(Currency currency);

    String deleteCurrency(long id);
}
