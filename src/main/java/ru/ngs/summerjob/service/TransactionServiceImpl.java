package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.TransactionDAO;
import ru.ngs.summerjob.dao.TransactionDAOImpl;
import ru.ngs.summerjob.entity.Transaction;

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
    public List<Transaction> getTransactionsByUserId(long id) {
        return transactionDAO.getTransactionsByUserId(id);
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        return transactionDAO.saveTransaction(transaction);
    }
}
