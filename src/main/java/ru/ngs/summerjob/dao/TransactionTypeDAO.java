package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.TransactionType;

public interface TransactionTypeDAO {
    TransactionType getTransactionTypeById(long id);
}
