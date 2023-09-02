package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Account;

import java.util.List;

public interface AccountService {
    Account getAccountById(long id);

    Account saveAccount(Account account);

    Account updateAccount(Account account);

    String deleteAccount(long id);
    List<Account> getAccountsByUserId(long userId);
    Account getAccountByName(String accountName);

    List<Account> getAccountsByBankId(long bankId);
}
