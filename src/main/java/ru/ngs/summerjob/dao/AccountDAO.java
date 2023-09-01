package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Account;

import java.util.List;

public interface AccountDAO {
    Account getAccountById(long id);
    List<Account> getAccountsByUserId(long userId);
    Account getAccountByName(String accountName);

    List<Account> getAccountByBankId(long bankId);
}
