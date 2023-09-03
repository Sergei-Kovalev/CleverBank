package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDAO {
    Transaction getTransactionById(long id);
    double getTotalIncome(Account account, LocalDateTime fromDate, LocalDateTime toDate);
    double getTotalOutcome(Account account, LocalDateTime fromDate, LocalDateTime toDate);

    Transaction saveTransactionForServlet(Transaction transaction);

    Transaction updateTransaction(Transaction transaction);

    String deleteTransactionById(long id);

    List<Transaction> getTransactionsByUserIdAndPeriod(long userId, LocalDateTime fromDate, LocalDateTime toDate);

    boolean saveTransaction(Transaction transaction);
}
