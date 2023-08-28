package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.AccountDAO;
import ru.ngs.summerjob.dao.AccountDAOImpl;
import ru.ngs.summerjob.entity.Account;

public class AccountServiceImpl implements AccountService {
    AccountDAO accountDAO;

    public AccountServiceImpl() {
        this.accountDAO = new AccountDAOImpl();
    }

    @Override
    public Account getAccountById(long id) {
        return accountDAO.getAccountById(id);
    }
}
