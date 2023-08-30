package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction getTransactionById(long id);
    List<Transaction> getTransactionsByUserId(long id);

    boolean saveTransaction(Transaction transaction);
}
