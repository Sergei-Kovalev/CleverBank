package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Transaction;

import java.util.List;

public interface TransactionDAO {
    Transaction getTransactionById(long id);
    List<Transaction> getTransactionsByUserId(long id);
}
