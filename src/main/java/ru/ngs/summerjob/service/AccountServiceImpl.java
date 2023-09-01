package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.AccountDAO;
import ru.ngs.summerjob.dao.AccountDAOImpl;
import ru.ngs.summerjob.entity.Account;

import java.util.List;

public class AccountServiceImpl implements AccountService {
    AccountDAO accountDAO;

    public AccountServiceImpl() {
        this.accountDAO = new AccountDAOImpl();
    }

    @Override
    public Account getAccountById(long id) {
        return accountDAO.getAccountById(id);
    }

    @Override
    public List<Account> getAccountsByUserId(long userId) {
        return accountDAO.getAccountsByUserId(userId);
    }

    @Override
    public Account getAccountByName(String accountName) {
        return accountDAO.getAccountByName(accountName);
    }

    @Override
    public List<Account> getAccountsByBankId(long bankId) {
        return accountDAO.getAccountByBankId(bankId);
    }
}
