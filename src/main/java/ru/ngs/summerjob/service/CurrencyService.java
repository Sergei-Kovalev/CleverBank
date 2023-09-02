package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Currency;

public interface CurrencyService {
    Currency getCurrencyById(long id);
    Currency saveCurrency(Currency currency);

    Currency updateCurrency(Currency currency);

    String deleteCurrency(long id);
}
