package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Bank;

public interface BankDAO {
    Bank getBankById(long id);

    Bank getBankByName(String name);

    Bank saveBank(Bank bank);

    Bank updateBank(Bank bank);

    String deleteBank(long id);
}
