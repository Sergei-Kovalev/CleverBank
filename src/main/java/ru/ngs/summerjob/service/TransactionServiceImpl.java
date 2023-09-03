package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.TransactionDAO;
import ru.ngs.summerjob.dao.TransactionDAOImpl;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    TransactionDAO transactionDAO;

    public TransactionServiceImpl() {
        this.transactionDAO = new TransactionDAOImpl();
    }

    @Override
    public Transaction getTransactionById(long id) {
        return transactionDAO.getTransactionById(id);
    }

    @Override
    public double getTotalIncome(Account account, LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionDAO.getTotalIncome(account, fromDate, toDate);
    }

    public double getTotalOutcome(Account account, LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionDAO.getTotalOutcome(account, fromDate, toDate);
    }

    @Override
    public Transaction saveTransactionForServlet(Transaction transaction) {
        return transactionDAO.saveTransactionForServlet(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionDAO.updateTransaction(transaction);
    }

    @Override
    public String deleteTransactionById(long id) {
        return transactionDAO.deleteTransactionById(id);
    }

    @Override
    public List<Transaction> getTransactionsByUserIdAndPeriod(long id, LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionDAO.getTransactionsByUserIdAndPeriod(id, fromDate, toDate);
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        return transactionDAO.saveTransaction(transaction);
    }
}
