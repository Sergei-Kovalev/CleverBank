package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDAO {
    Transaction getTransactionById(long id);
    List<Transaction> getTransactionsByUserIdAndPeriod(long userId, LocalDateTime fromDate, LocalDateTime toDate);

    boolean saveTransaction(Transaction transaction);
}
