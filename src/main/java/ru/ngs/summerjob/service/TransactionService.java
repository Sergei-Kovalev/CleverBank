package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    Transaction getTransactionById(long id);
    List<Transaction> getTransactionsByUserIdAndPeriod(long id, LocalDateTime fromDate, LocalDateTime toDate);

    boolean saveTransaction(Transaction transaction);
}
