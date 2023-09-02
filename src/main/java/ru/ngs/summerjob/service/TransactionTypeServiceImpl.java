package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.TransactionTypeDAO;
import ru.ngs.summerjob.dao.TransactionTypeDAOImpl;
import ru.ngs.summerjob.entity.TransactionType;

public class TransactionTypeServiceImpl implements TransactionTypeService {

    TransactionTypeDAO transactionTypeDAO;

    public TransactionTypeServiceImpl() {
        this.transactionTypeDAO = new TransactionTypeDAOImpl();
    }

    @Override
    public TransactionType getTransactionTypeById(long id) {
        return transactionTypeDAO.getTransactionTypeById(id);
    }

    @Override
    public TransactionType saveTransactionType(TransactionType transactionType) {
        return transactionTypeDAO.saveTransactionType(transactionType);
    }

    @Override
    public TransactionType updateTransactionType(TransactionType transactionType) {
        return transactionTypeDAO.updateTransactionType(transactionType);
    }

    @Override
    public String deleteTransactionTypeById(long id) {
        return transactionTypeDAO.deleteTransactionTypeById(id);
    }

}
