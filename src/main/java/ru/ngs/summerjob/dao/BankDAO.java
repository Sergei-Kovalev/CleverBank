package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Bank;

public interface BankDAO {
    Bank getBankById(long id);

    Bank getBankByName(String name);
}
