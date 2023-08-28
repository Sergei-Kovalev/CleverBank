package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Account;

public interface AccountDAO {
    Account getAccountById(long id);
}
