package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Bank;

public interface BankService {
    Bank getBankById(long id);

    Bank getBankByName(String name);
    Bank saveBank(Bank bank);

    Bank updateBank(Bank bank);

    String deleteBank(long id);
}
